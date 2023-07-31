package com.github.mishaninss.arma.data.comparator.checker;

import com.github.mishaninss.arma.data.comparator.ComparisonStatus;
import com.github.mishaninss.arma.data.comparator.FieldComparisonResult;
import org.apache.commons.lang3.StringUtils;

public class EqualsIgnoreCaseChecker implements FieldChecker {

  @Override
  public FieldComparisonResult check(Object expected, Object actual) throws AssertionError {
    FieldComparisonResult result = new FieldComparisonResult();
    result.setExpected(expected);
    result.setActual(actual);
    result.setСomment("Strings to be equal ignoring case");
    result.setStatus(ComparisonStatus.PASS);
    if (expected == null && actual == null) {
      return result;
    }
    if (expected != null && actual != null && StringUtils.equalsIgnoreCase(expected.toString(),
        actual.toString())) {
      return result;
    }
    result.setStatus(ComparisonStatus.FAIL);
    return result;
  }
}
