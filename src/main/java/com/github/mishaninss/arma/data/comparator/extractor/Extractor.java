package com.github.mishaninss.arma.data.comparator.extractor;

import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Extractor {
  Object extract(Object object, String field);
}
