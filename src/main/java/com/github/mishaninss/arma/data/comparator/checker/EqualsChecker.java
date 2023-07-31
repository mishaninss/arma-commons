package com.github.mishaninss.arma.data.comparator.checker;

import com.github.mishaninss.arma.data.comparator.ComparisonStatus;
import com.github.mishaninss.arma.data.comparator.FieldComparisonResult;
import java.util.Objects;

public class EqualsChecker implements FieldChecker {

  @Override
  public FieldComparisonResult check(Object expected, Object actual) throws AssertionError {
    FieldComparisonResult result = new FieldComparisonResult();
    result.setExpected(expected);
    result.setActual(actual);
    result.setСomment("Objects to be equal");
    result.setStatus(ComparisonStatus.PASS);
    if (!Objects.equals(expected, actual)) {
      result.setStatus(ComparisonStatus.FAIL);
    }
    return result;
  }
}
