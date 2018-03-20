/*
 *
 * Copyright 2018 Sergey Mishanin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.mishaninss.data;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CsvDataExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvDataExtractor.class);

    private CsvDataExtractor() {}

    public static Map<String, List<String>> extractData(URL resourceUrl) throws IOException {
        return extractData(resourceUrl.getFile());
    }

    public static Map<String, List<String>> extractData(String pathToFile) throws IOException {
        if (StringUtils.isBlank(pathToFile)){
            throw new IllegalArgumentException("Path to com.github.data file cannot be empty");
        }

        URL locatorsUrl = CsvDataExtractor.class.getClassLoader().getResource(pathToFile);
        if (locatorsUrl != null){
            return extractData(locatorsUrl);
        } else {
            return extractData(new File(pathToFile));
        }
    }

    public static Map<String, List<String>> extractData(File file) throws IOException {
        if (!file.exists()){
            throw new IOException("Cannot load data from file [" + file.getCanonicalPath() + "]. File doesn't exist");
        }

        Map<String, List<String>> data = new LinkedHashMap<>();

        try (FileReader fr = new FileReader(file)) {
            CSVReader reader = new CSVReader(fr);
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length > 0) {
                    String key = nextLine[0];
                    List<String> values = new ArrayList<>(Arrays.asList(nextLine));
                    values.remove(0);
                    data.put(key, values);
                }
            }
        }

        return data;
    }

    public static String[][] extractDataAsArray(String pathToFile, String... ignoredColumnNames) throws IOException {
        return extractDataAsArray(new File(pathToFile), ignoredColumnNames);
    }

    public static String[][] extractDataAsArray(String pathToFile) throws IOException {
        return extractDataAsArray(pathToFile, new String[0]);
    }

    public static String[][] extractDataAsArray(File file) throws IOException {
        return extractDataAsArray(file, new String[0]);
    }

    public static String[][] extractDataAsArray(File file, String... ignoredColumnNames) throws IOException {
        if (!file.exists()){
            throw new IOException("Cannot load com.github.data from file [" + file.getCanonicalPath() + "]. File doesn't exist");
        }

        List<List<String>> dataList = extractDataAsList(file, ignoredColumnNames);
        if (CollectionUtils.isEmpty(dataList) || CollectionUtils.isEmpty(dataList.get(0))){
            return new String[0][0];
        }
        int rowCount = dataList.size();
        int colCount = dataList.get(0).size();
        String[][] data = new String[rowCount][colCount];

        for (int i=0; i<rowCount; i++){
            List<String> row = dataList.get(i);
            int maxIndex = row.size() < colCount ? row.size() : colCount;
            for (int j=0; j< maxIndex; j++){
                data[i][j] = StringUtils.defaultString(row.get(j).trim());
            }
        }

        return data;
    }

    public static List<List<String>> extractDataAsList(String pathToFile) throws IOException {
        if (StringUtils.isBlank(pathToFile)){
            throw new IllegalArgumentException("Path to com.github.data file cannot be empty");
        }
        URL resourceUrl = CsvDataExtractor.class.getClassLoader().getResource(pathToFile);
        if (resourceUrl != null) {
            return extractDataAsList(new File(resourceUrl.getFile()));
        } else {
            throw new RuntimeException("resource [" + pathToFile + "] was not found");
        }
    }


    public static List<List<String>> extractDataAsList(File file) throws IOException{
        return extractDataAsList(file, new String[0]);
    }

    public static List<List<String>> extractDataAsList(File file, String... ignoredColumnNames) throws IOException {
        if (!file.exists()){
            throw new IOException("Cannot load com.github.data from file [" + file.getCanonicalPath() + "]. File doesn't exist");
        }

        List<List<String>> data = new ArrayList<>();

        try (FileReader fr = new FileReader(file)) {
            try(CSVReader reader = new CSVReader(fr)) {
                String[] nextLine;
                while ((nextLine = reader.readNext()) != null) {
                    if (nextLine.length > 0) {
                        List<String> values = new ArrayList<>(Arrays.asList(StringUtils.stripAll(nextLine)));
                        data.add(values);
                    }
                }
            }
        }

        if (ignoredColumnNames.length > 0 && CollectionUtils.isNotEmpty(data)){
            List<String> header = data.get(0);
            for(int i=0; i<header.size(); i++){
                header.set(i, header.get(i).trim());
            }
            for (String ignoredColumName: ignoredColumnNames){
                int colIndex = header.indexOf(ignoredColumName.trim());
                if (colIndex > -1){
                    data.forEach(row -> row.remove(colIndex));
                }
            }
        }

        return data;
    }

    public static void createCsvFile(String path, List<String[]> allLines){
        try (BufferedWriter fr = Files.newBufferedWriter(Paths.get(path));
             CSVWriter writer = new CSVWriter(fr)) {
            writer.writeAll(allLines, true);
            writer.flushQuietly();
            LOGGER.info("{} file has been created", path);
        } catch (Exception ex){
            LOGGER.error("Could not write to [" + path + "] file", ex);
        }
    }

    public static void saveMapWithListValueToCsvFile(String path, Map<String, List<String>> allLines){
        List<String[]> lines = new LinkedList<>();
        allLines.forEach((key, value) -> {
            String[] line = new String[1];
            line[0] = key;
            line = ArrayUtils.addAll(line, value.toArray(new String[0]));
            lines.add(line);
        });
        createCsvFile(path, lines);
    }

    public static void saveMapToCsvFile(String path, Map<String, ?> allLines){
        List<String[]> lines = new LinkedList<>();
        allLines.forEach((key, value) -> {
            String[] line = new String[1];
            line[0] = key;
            line = ArrayUtils.addAll(line, value.toString());
            lines.add(line);
        });
        createCsvFile(path, lines);
    }
}
