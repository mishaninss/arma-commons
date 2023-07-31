package com.github.mishaninss.arma.data.comparator.checker;

import com.github.mishaninss.arma.data.comparator.FieldComparisonResult;

@FunctionalInterface
public interface FieldChecker {
    FieldComparisonResult check(Object a, Object b) throws AssertionError;
}
