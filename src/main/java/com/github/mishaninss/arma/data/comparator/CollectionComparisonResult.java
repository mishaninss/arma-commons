package com.github.mishaninss.arma.data.comparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;

import static com.github.mishaninss.arma.data.comparator.ComparisonStatus.FAIL;

public class CollectionComparisonResult<T> {

  private Map<Integer, ObjectComparisonResult> results;
  private List<T> expected;
  private List<T> actual;
  private String comment;
  private ComparisonStatus status = ComparisonStatus.PASS;

  public CollectionComparisonResult() {
  }


  public synchronized Map<Integer, ObjectComparisonResult> getResults() {
    return results;
  }

  public synchronized void addResult(int index, ObjectComparisonResult result) {
    if (results == null) {
      results = new HashMap<>();
    }
    results.put(index, result);
    if (FAIL.equals(result.getStatus())) {
      setStatus(FAIL);
    }
  }

  public synchronized CollectionComparisonResult setResults(
      Map<Integer, ObjectComparisonResult> results) {
    this.results = results;
    return this;
  }

  public String getComment() {
    return comment;
  }

  public CollectionComparisonResult setComment(String comment) {
    this.comment = comment;
    return this;
  }

  public ComparisonStatus getStatus() {
    return status;
  }

  public CollectionComparisonResult setStatus(
      ComparisonStatus status) {
    this.status = status;
    return this;
  }

  public List<T> getExpected() {
    return expected;
  }

  public CollectionComparisonResult setExpected(List<T> expected) {
    this.expected = expected;
    return this;
  }

  public List<T> getActual() {
    return actual;
  }

  public CollectionComparisonResult setActual(List<T> actual) {
    this.actual = actual;
    return this;
  }
}
