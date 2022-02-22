package com.github.mishaninss.arma.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public final class FileUtils {

    private FileUtils() {
    }

    /**
     * Возвращает объекн File для заданного пути к файлу.
     * Путь к файлу может быть задан как идентификатор ресурса или как путь на файловой системе.
     *
     * @param path - путь к файлу
     * @return - объект File для заданного пути
     */
    public static File getFile(String path) {
        URL url = FileUtils.class.getResource(path);
        if (url != null) {
            return new File(url.getPath());
        } else {
            return new File(path);
        }
    }

    public static InputStream getInputStream(String path) throws FileNotFoundException {
      URL url = FileUtils.class.getResource(path);
      if (url != null) {
        return FileUtils.class.getResourceAsStream(path);
      } else {
        return new FileInputStream(new File(path));
      }
    }
}
