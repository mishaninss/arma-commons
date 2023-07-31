package com.github.mishaninss.arma.data.comparator;

import com.github.mishaninss.arma.data.comparator.checker.ICollectionChecker;
import com.github.mishaninss.arma.data.comparator.reporter.CollectionHtmlResultReporter;
import com.github.mishaninss.arma.data.comparator.reporter.CollectionResultReporter;
import java.util.List;

public class CollectionChecker<T> {

  private String name;
  private CollectionComparisonResult result;
  private CollectionResultReporter reporter = new CollectionHtmlResultReporter("target/comparison");
  private ICollectionChecker checker;


  public CollectionChecker withChecker(ICollectionChecker checker) {
    this.checker = checker;
    return this;
  }

  public CollectionChecker withReporter(CollectionResultReporter reporter) {
    this.reporter = reporter;
    return this;
  }

  public CollectionChecker as(String name) {
    this.name = name;
    return this;
  }

  public CollectionChecker compare(List<T> expected, List<T> actual) {
    result = checker.check(expected, actual);
    result.setComment(name);
    return this;
  }

  public void report() {
    reporter.report(result);
  }

  public CollectionComparisonResult getResult() {
    return result;
  }

}