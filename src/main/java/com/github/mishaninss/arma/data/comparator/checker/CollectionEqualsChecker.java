package com.github.mishaninss.arma.data.comparator.checker;

import com.github.mishaninss.arma.data.comparator.CollectionComparisonResult;
import com.github.mishaninss.arma.data.comparator.ComparisonStatus;
import com.github.mishaninss.arma.data.comparator.ObjectChecker;
import java.util.List;

public class CollectionEqualsChecker implements ICollectionChecker {

  private final ObjectChecker checker;

  public CollectionEqualsChecker(ObjectChecker checker) {
    this.checker = checker;
  }

  @Override
  public <T> CollectionComparisonResult check(List<T> a, List<T> b) {
    var result = new CollectionComparisonResult();
    result.setExpected(a);
    result.setActual(b);
    if (a.size() != b.size()) {
      result.setComment(
          "Collections sizes are not equal. Expected " + a.size() + " but was " + b.size());
      result.setStatus(ComparisonStatus.FAIL);
      return result;
    }

    for (int i = 0; i < a.size(); i++) {
      var expected = a.get(i);
      var actual = b.get(i);
      var iResult = checker.compare(expected, actual).getResult();
      result.addResult(i + 1, iResult);
    }

    return result;

  }
}
