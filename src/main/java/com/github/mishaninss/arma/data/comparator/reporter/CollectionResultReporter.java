package com.github.mishaninss.arma.data.comparator.reporter;

import com.github.mishaninss.arma.data.comparator.CollectionComparisonResult;
import com.github.mishaninss.arma.data.comparator.ObjectComparisonResult;
import org.apache.poi.ss.formula.functions.T;

@FunctionalInterface
public interface CollectionResultReporter<T> {

  void report(CollectionComparisonResult<T> result);
}
