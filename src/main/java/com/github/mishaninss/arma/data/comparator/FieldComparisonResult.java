package com.github.mishaninss.arma.data.comparator;

public class FieldComparisonResult {

  private String field;
  private Object expected;
  private Object actual;

  private String сomment;

  private ComparisonStatus status;

  public FieldComparisonResult() {
  }

  public FieldComparisonResult(String field, Object expected, Object actual, String сomment) {
    this.field = field;
    this.expected = expected;
    this.actual = actual;
    this.сomment = сomment;
  }

  public String getField() {
    return field;
  }

  public FieldComparisonResult setField(String field) {
    this.field = field;
    return this;
  }

  public Object getExpected() {
    return expected;
  }

  public FieldComparisonResult setExpected(Object expected) {
    this.expected = expected;
    return this;
  }

  public Object getActual() {
    return actual;
  }

  public FieldComparisonResult setActual(Object actual) {
    this.actual = actual;
    return this;
  }

  public String getСomment() {
    return сomment;
  }

  public FieldComparisonResult setСomment(String сomment) {
    this.сomment = сomment;
    return this;
  }

  public ComparisonStatus getStatus() {
    return status;
  }

  public FieldComparisonResult setStatus(
      ComparisonStatus status) {
    this.status = status;
    return this;
  }
}
