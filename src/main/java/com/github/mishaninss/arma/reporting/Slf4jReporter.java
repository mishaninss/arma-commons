package com.github.mishaninss.arma.reporting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class Slf4jReporter implements IReporter {
    private Logger logger = LoggerFactory.getLogger(Slf4jReporter.class);
    private static final String ATTACHMENTS_WARNING = "Вложения не поддерживаются";

    @Override
    public void setReporterName(String reporterName) {
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
        if (logger.isTraceEnabled()) {
            String message = getMassage(msg, args);
            logger.trace(message);
        }
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
        if (logger.isDebugEnabled()) {
            String message = getMassage(msg, args);
            logger.debug(message);
        }
    }

    @Override
    public void debug(String msg, Throwable e) {
        logger.debug(msg, e);
    }

    @Override
    public void info(String msg, Object... args) {
        if (logger.isInfoEnabled()) {
            String message = getMassage(msg, args);
            logger.info(message);
        }
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
        if (logger.isWarnEnabled()) {
            String message = getMassage(msg, args);
            logger.warn(message);
        }
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
        if (logger.isErrorEnabled()) {
            String message = getMassage(msg, args);
            logger.error(message);
        }
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

    private String getMassage(String format, Object... args) {
        return String.format(format, args);
    }
}