package com.github.mishaninss.arma.data.comparator.checker;

import com.github.mishaninss.arma.data.comparator.CollectionComparisonResult;
import java.util.List;

@FunctionalInterface
public interface ICollectionChecker {

  <T> CollectionComparisonResult check(List<T> a, List<T> b);
}
