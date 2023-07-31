package com.github.mishaninss.arma.data.comparator.checker;

import com.github.mishaninss.arma.data.comparator.ComparisonStatus;
import com.github.mishaninss.arma.data.comparator.FieldComparisonResult;
import java.util.function.BiPredicate;

public class FunctionChecker implements FieldChecker {

  private BiPredicate<Object, Object> function;
  private String message = "Field comparison was failed";

  public FunctionChecker(BiPredicate<Object, Object> function) {
    this.function = function;
  }

  public FunctionChecker(BiPredicate<Object, Object> function, String message) {
    this.function = function;
    this.message = message;
  }

  @Override
  public FieldComparisonResult check(Object expected, Object actual) throws AssertionError {
    FieldComparisonResult result = new FieldComparisonResult();
    result.setExpected(expected);
    result.setActual(actual);
    result.setСomment(message);
    result.setStatus(ComparisonStatus.PASS);
    if (!function.test(expected, actual)) {
      result.setStatus(ComparisonStatus.FAIL);
    }
    return result;
  }
}
