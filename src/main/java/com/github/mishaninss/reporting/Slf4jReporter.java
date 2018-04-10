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
    private static final Logger LOGGER = LoggerFactory.getLogger(Slf4jReporter.class);
    private static final String ATTACHMENTS_WARNING = "Attachments are not supported";

    @Override
    public void attachScreenshot(byte[] screenshot, String msg) {
        LOGGER.warn(ATTACHMENTS_WARNING);
    }

    @Override
    public void attachScreenshot(byte[] screenshot) {
        LOGGER.warn(ATTACHMENTS_WARNING);
    }

    @Override
    public void attachFile(String pathToFile, String msg) {
        LOGGER.warn(ATTACHMENTS_WARNING);
    }

    @Override
    public void attachText(String text, String msg) {
        LOGGER.warn(ATTACHMENTS_WARNING);
    }

    @Override
    public void trace(String msg) {
        LOGGER.trace(msg);
    }

    @Override
    public void trace(String msg, Object... args) {
        LOGGER.trace(msg, args);
    }

    @Override
    public void trace(String msg, Throwable e) {
        LOGGER.trace(msg, e);
    }

    @Override
    public void debug(String msg) {
        LOGGER.debug(msg);
    }

    @Override
    public void debug(String msg, Object... args) {
        LOGGER.debug(msg, args);
    }

    @Override
    public void debug(String msg, Throwable e) {
        LOGGER.debug(msg, e);
    }

    @Override
    public void info(String msg, Object... args) {
        LOGGER.info(msg, args);
    }

    @Override
    public void info(String msg) {
        LOGGER.info(msg);
    }

    @Override
    public void info(String msg, Throwable e) {
        LOGGER.info(msg, e);
    }

    @Override
    public void warn(String msg, Object... args) {
        LOGGER.warn(msg, args);
    }

    @Override
    public void warn(String msg) {
        LOGGER.warn(msg);
    }

    @Override
    public void warn(String msg, Throwable e) {
        LOGGER.warn(msg, e);
    }

    @Override
    public void error(String msg, Object... args) {
        LOGGER.error(msg, args);
    }

    @Override
    public void error(String msg) {
        LOGGER.error(msg);
    }

    @Override
    public void error(String msg, Throwable e) {
        LOGGER.error(msg, e);
    }

    @Override
    public void ignoredException(Exception ex) {
        trace("Ignored exception", ex);
    }
}
