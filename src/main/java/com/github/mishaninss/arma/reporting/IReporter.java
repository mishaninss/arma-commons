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

package com.github.mishaninss.arma.reporting;

public interface IReporter {
    String QUALIFIER = "IReporter";

    void setReporterName(String reporterName);

    void attachScreenshot(byte[] screenshot, String msg);

    void attachScreenshot(byte[] screenshot);

    void attachFile(String pathToFile, String msg);

    void attachText(String text, String msg);

    void trace(String msg);

    void trace(String msg, Object... args);

    void trace(String msg, Throwable e);

    void debug(String msg, Object... args);

    void debug(String msg);

    void debug(String msg, Throwable e);

    void info(String msg, Object... args);

    void info(String msg);

    void info(String msg, Throwable e);

    void warn(String msg, Object... args);

    void warn(String msg);

    void warn(String msg, Throwable e);

    void error(String msg, Object... args);

    void error(String msg);

    void error(String msg, Throwable e);

    void ignoredException(Exception ex);
}
