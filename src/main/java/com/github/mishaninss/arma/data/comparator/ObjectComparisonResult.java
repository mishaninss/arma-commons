package com.github.mishaninss.arma.data.comparator;

import java.util.ArrayList;
import java.util.List;

import static com.github.mishaninss.arma.data.comparator.ComparisonStatus.FAIL;

public class ObjectComparisonResult {

  private List<FieldComparisonResult> results;
  private String comment;
  private ComparisonStatus status = ComparisonStatus.PASS;

  public ObjectComparisonResult() {
  }

  public ObjectComparisonResult(List<FieldComparisonResult> results, String comment) {
    this.results = results;
    this.comment = comment;
  }

  public synchronized List<FieldComparisonResult> getResults() {
    return results;
  }

  public synchronized void addResult(FieldComparisonResult result) {
    if (results == null) {
      results = new ArrayList<>();
    }
    results.add(result);
    if (FAIL.equals(result.getStatus())) {
      setStatus(FAIL);
    }
  }

  public synchronized ObjectComparisonResult setResults(
      List<FieldComparisonResult> results) {
    this.results = results;
    return this;
  }

  public String getComment() {
    return comment;
  }

  public ObjectComparisonResult setComment(String comment) {
    this.comment = comment;
    return this;
  }

  public ComparisonStatus getStatus() {
    return status;
  }

  public ObjectComparisonResult setStatus(
      ComparisonStatus status) {
    this.status = status;
    return this;
  }

  public FieldComparisonResult getFiledResult(String field) {
    return results.stream().filter(r -> r.getField().equalsIgnoreCase(field))
        .findFirst()
        .orElse(null);
  }
}
