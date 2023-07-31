package com.github.mishaninss.arma.data.comparator;

import com.github.mishaninss.arma.data.comparator.checker.FieldChecker;
import com.github.mishaninss.arma.data.comparator.extractor.Extractor;

public class CheckSpec {

  private Extractor extractor;
  private FieldChecker checker;
  private boolean checkNulls;

  public Extractor getExtractor() {
    return extractor;
  }

  public CheckSpec setExtractor(
      Extractor extractor) {
    this.extractor = extractor;
    return this;
  }

  public FieldChecker getChecker() {
    return checker;
  }

  public CheckSpec setChecker(FieldChecker checker) {
    this.checker = checker;
    return this;
  }

  public boolean isCheckNulls() {
    return checkNulls;
  }

  public CheckSpec setCheckNulls(boolean checkNulls) {
    this.checkNulls = checkNulls;
    return this;
  }
}
