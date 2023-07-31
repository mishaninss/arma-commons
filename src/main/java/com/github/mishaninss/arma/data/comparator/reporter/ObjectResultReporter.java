package com.github.mishaninss.arma.data.comparator.reporter;

import com.github.mishaninss.arma.data.comparator.ObjectComparisonResult;

@FunctionalInterface
public interface ObjectResultReporter {

  void report(ObjectComparisonResult result);
}
