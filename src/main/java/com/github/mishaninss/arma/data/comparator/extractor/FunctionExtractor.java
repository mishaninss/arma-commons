package com.github.mishaninss.arma.data.comparator.extractor;

import java.util.function.UnaryOperator;

public class FunctionExtractor implements Extractor {

  private final UnaryOperator<Object> function;

  public FunctionExtractor(UnaryOperator<Object> function) {
    this.function = function;
  }

  @Override
  public Object extract(Object object, String fieldName) {
    return function.apply(object);
  }
}
