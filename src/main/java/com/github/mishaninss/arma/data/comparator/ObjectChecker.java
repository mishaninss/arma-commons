package com.github.mishaninss.arma.data.comparator;

import com.github.mishaninss.arma.data.comparator.checker.FieldChecker;
import com.github.mishaninss.arma.data.comparator.checker.EqualsChecker;
import com.github.mishaninss.arma.data.comparator.checker.FunctionChecker;
import com.github.mishaninss.arma.data.comparator.extractor.Extractor;
import com.github.mishaninss.arma.data.comparator.extractor.FiledExtractor;
import com.github.mishaninss.arma.data.comparator.extractor.FunctionExtractor;
import com.github.mishaninss.arma.data.comparator.reporter.ObjectResultReporter;
import com.github.mishaninss.arma.data.comparator.reporter.SimpleObjectResultReporter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.UnaryOperator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

public class ObjectChecker {

  private String name;
  private boolean checkExpectedNulls;

  private ObjectComparisonResult result;

  private final Map<String, CheckSpec> fieldSpec = new HashMap<>();
  private final Map<Class<?>, FieldChecker> typedCheckers = new HashMap<>();

  private ObjectResultReporter reporter = new SimpleObjectResultReporter();

  public ObjectChecker forClass(Class<?> clazz) {
    FieldUtils.getAllFieldsList(clazz).forEach(f -> {
      String fieldName = f.getName();
      forField(fieldName);
    });
    return this;
  }

  public ObjectChecker forFieldType(Class<?> type, FieldChecker checker) {
    typedCheckers.put(type, checker);
    return this;
  }

  public ObjectChecker as(String name) {
    this.name = name;
    return this;
  }

  public ObjectChecker checkExpectedNulls() {
    checkExpectedNulls = true;
    fieldSpec.values().forEach(spec -> spec.setCheckNulls(true));
    return this;
  }

  public ObjectChecker withReporter(ObjectResultReporter reporter) {
    this.reporter = reporter;
    return this;
  }

  public ObjectChecker forField(String fieldName) {
    return forField(fieldName, new FiledExtractor(), null, checkExpectedNulls);
  }

  public ObjectChecker forField(String fieldName, boolean checkNulls) {
    return forField(fieldName, new FiledExtractor(), null, checkNulls);
  }

  public ObjectChecker forField(String fieldName, Extractor extractor) {
    return forField(fieldName, extractor, null, checkExpectedNulls);
  }

  public ObjectChecker forField(String fieldName, UnaryOperator<Object> extractor) {
    return forField(fieldName, new FunctionExtractor(extractor), null,
        checkExpectedNulls);
  }

  public ObjectChecker forField(String fieldName, FieldChecker checker) {
    return forField(fieldName, new FiledExtractor(), checker, checkExpectedNulls);
  }

  public ObjectChecker forField(String fieldName, BiPredicate<Object, Object> checker) {
    return forField(fieldName, new FiledExtractor(), new FunctionChecker(checker),
        checkExpectedNulls);
  }

  public ObjectChecker forField(String fieldName, BiPredicate<Object, Object> checker,
      String message) {
    return forField(fieldName, new FiledExtractor(), new FunctionChecker(checker, message),
        checkExpectedNulls);
  }

  public ObjectChecker forField(String fieldName, Extractor extractor, FieldChecker checker,
      boolean checkNulls) {
    var specs = new CheckSpec()
        .setExtractor(extractor)
        .setChecker(checker)
        .setCheckNulls(checkNulls);
    fieldSpec.put(fieldName, specs);
    return this;
  }

  public ObjectChecker excluding(String... fieldNames) {
    for (String fieldName : fieldNames) {
      fieldSpec.remove(fieldName);
    }
    return this;
  }

  public ObjectChecker compare(Object expected, Object actual) {
    if (fieldSpec.isEmpty()) {
      forClass(expected.getClass());
    }
    List<String> errorLog = new ArrayList<>();
    if (StringUtils.isNotBlank(name)) {
      errorLog.add(name);
    }
    result = new ObjectComparisonResult();
    result.setComment(name);
    fieldSpec.forEach((field, spec) -> {
      Object expectedVal = spec.getExtractor().extract(expected, field);
      if (expectedVal != null || spec.isCheckNulls()) {
        Object actualVal = spec.getExtractor().extract(actual, field);
        var checker = getChecker(expected.getClass(), field, spec);
        var fResult = checkValues(expectedVal, actualVal, checker);
        fResult.setField(field);
        result.addResult(fResult);
      }
    });
    return this;
  }

  public void report() {
    reporter.report(result);
  }

  public ObjectComparisonResult getResult() {
    return result;
  }

  private FieldComparisonResult checkValues(Object expected, Object actual, FieldChecker checker) {
    return checker.check(expected, actual);
  }

  private FieldChecker getChecker(Class<?> clazz, String fieldName, CheckSpec spec) {
    if (spec.getChecker() != null) {
      return spec.getChecker();
    }
    var checker = typedCheckers.get(FieldUtils.getField(clazz, fieldName, true).getType());
    return checker != null ? checker : new EqualsChecker();
  }

}