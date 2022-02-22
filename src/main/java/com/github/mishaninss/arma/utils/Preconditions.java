package com.github.mishaninss.arma.utils;

import org.apache.commons.lang3.StringUtils;

import static java.lang.String.format;

public class Preconditions {

    private Preconditions() {
    }

    public static void checkNotNull(Object arg, String argumentName) {
        if (arg == null) {
            throw new IllegalArgumentException(format("Argument [%s] must not be null", argumentName));
        }
    }

    public static void checkNotBlank(String arg, String argumentName) {
        if (StringUtils.isBlank(arg)) {
            throw new IllegalArgumentException(format("Argument [%s] must not be null or blank string", argumentName));
        }
    }
}
