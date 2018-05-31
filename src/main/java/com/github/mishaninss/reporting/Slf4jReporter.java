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

package com.github.mishaninss.reporting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class Slf4jReporter implements IReporter{
    private Logger logger = LoggerFactory.getLogger(Slf4jReporter.class);
    private static final String ATTACHMENTS_WARNING = "Attachments are not supported";

    @Override
    public void setReporterName(String reporterName){
        logger = LoggerFactory.getLogger(reporterName);
    }

    @Override
    public void attachScreenshot(byte[] screenshot, String msg) {
        logger.warn(ATTACHMENTS_WARNING);
    }

    @Override
    public void attachScreenshot(byte[] screenshot) {
        logger.warn(ATTACHMENTS_WARNING);
    }

    @Override
    public void attachFile(String pathToFile, String msg) {
        logger.warn(ATTACHMENTS_WARNING);
    }

    @Override
    public void attachText(String text, String msg) {
        logger.warn(ATTACHMENTS_WARNING);
    }

    @Override
    public void trace(String msg) {
        logger.trace(msg);
    }

    @Override
    public void trace(String msg, Object... args) {
        logger.trace(msg, args);
    }

    @Override
    public void trace(String msg, Throwable e) {
        logger.trace(msg, e);
    }

    @Override
    public void debug(String msg) {
        logger.debug(msg);
    }

    @Override
    public void debug(String msg, Object... args) {
        logger.debug(msg, args);
    }

    @Override
    public void debug(String msg, Throwable e) {
        logger.debug(msg, e);
    }

    @Override
    public void info(String msg, Object... args) {
        logger.info(msg, args);
    }

    @Override
    public void info(String msg) {
        logger.info(msg);
    }

    @Override
    public void info(String msg, Throwable e) {
        logger.info(msg, e);
    }

    @Override
    public void warn(String msg, Object... args) {
        logger.warn(msg, args);
    }

    @Override
    public void warn(String msg) {
        logger.warn(msg);
    }

    @Override
    public void warn(String msg, Throwable e) {
        logger.warn(msg, e);
    }

    @Override
    public void error(String msg, Object... args) {
        logger.error(msg, args);
    }

    @Override
    public void error(String msg) {
        logger.error(msg);
    }

    @Override
    public void error(String msg, Throwable e) {
        logger.error(msg, e);
    }

    @Override
    public void ignoredException(Exception ex) {
        trace("Ignored exception", ex);
    }
}
