/*
 * Copyright (c) 2021 Sergey Mishanin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.mishaninss.arma.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XlsxDataExtractor {

  private static final Logger LOGGER = LoggerFactory.getLogger(XlsxDataExtractor.class);

  private XlsxDataExtractor() {
  }

  public static String[][] extractDataAsArray(File file, String... ignoredColumnNames)
      throws IOException {
    if (!file.exists()) {
      throw new IOException(
          "Cannot load data from file [" + file.getCanonicalPath() + "]. File doesn't exist");
    }

    List<List<String>> dataList = extractDataAsList(file, ignoredColumnNames);
    if (CollectionUtils.isEmpty(dataList) || CollectionUtils.isEmpty(dataList.get(0))) {
      return new String[0][0];
    }
    int rowCount = dataList.size();
    int colCount = dataList.get(0).size();
    String[][] data = new String[rowCount][colCount];

    for (int i = 0; i < rowCount; i++) {
      List<String> row = dataList.get(i);
      for (int j = 0; j < colCount; j++) {
        data[i][j] = j < row.size() ? StringUtils.defaultString(row.get(j).trim()) : "";
      }
    }

    return data;
  }

  public static List<Map<String, String>> extractDataAsListOfMaps(File file) throws IOException {
    List<List<String>> data = extractDataAsList(file);
    List<Map<String, String>> result = new ArrayList<>();
    if (data.size() > 1) {
      List<String> header = data.get(0);
      for (int i = 1; i < data.size(); i++) {
        List<String> row = data.get(i);
        Map<String, String> rowMap = new LinkedHashMap<>();
        for (int j = 0; j < row.size(); j++) {
          rowMap.put(header.get(j), row.get(j));
        }
        result.add(rowMap);
      }
    }
    return result;
  }

  public static List<List<String>> extractDataAsList(File file, String... ignoredColumnNames)
      throws IOException {
    if (!file.exists()) {
      throw new IOException(
          "Cannot load data from file [" + file.getAbsolutePath() + "]. File doesn't exist");
    }

    List<List<String>> data = new ArrayList<>();

    try (FileInputStream fis = new FileInputStream(file)) {
      XSSFWorkbook myExcelBook = new XSSFWorkbook(fis);
      XSSFSheet myExcelSheet = myExcelBook.getSheet("data");
      Iterator<Row> rowIterator = myExcelSheet.rowIterator();

      while (rowIterator.hasNext()) {
        List<String> rowValues = new LinkedList<>();
        Row row = rowIterator.next();
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
          Cell cell = cellIterator.next();
          String value = "";
          switch (cell.getCellTypeEnum()) {
            case STRING:
              value = cell.getStringCellValue();
              break;
            case NUMERIC:
              value = String.valueOf(cell.getNumericCellValue());
              break;
            case BOOLEAN:
              value = String.valueOf(cell.getBooleanCellValue());
              break;
            default:
              LOGGER.debug("Unknown cell type: {}", cell.getCellTypeEnum());
          }
          int colIndex = cell.getColumnIndex();
          if (colIndex > rowValues.size()) {
            for (int i = rowValues.size(); i < colIndex; i++) {
              rowValues.add("");
            }
          }
          rowValues.add(colIndex, value);
        }
        data.add(rowValues);
      }
      myExcelBook.close();
    }

    if (ignoredColumnNames.length > 0 && CollectionUtils.isNotEmpty(data)) {
      List<String> header = data.get(0);
      for (int i = 0; i < header.size(); i++) {
        header.set(i, header.get(i).trim());
      }
      for (String ignoredColumName : ignoredColumnNames) {
        int colIndex = header.indexOf(ignoredColumName.trim());
        if (colIndex > -1) {
          data.forEach(row -> row.remove(colIndex));
        }
      }
    }

    return data;
  }
}
