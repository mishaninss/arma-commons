package com.github.mishaninss.arma.data.comparator.reporter;

import com.github.mishaninss.arma.data.comparator.ObjectComparisonResult;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

import static com.github.mishaninss.arma.data.comparator.ComparisonStatus.FAIL;

public class SimpleObjectResultReporter implements ObjectResultReporter {

  @Override
  public void report(ObjectComparisonResult result) {
    if (FAIL.equals(result.getStatus())) {
      var message = result.getResults().stream()
          .filter(r -> FAIL.equals(r.getStatus()))
          .map(
              r -> "Field [" + r.getField() + "] " + r.getСomment() + "\nExpected: "
                  + r.getExpected()
                  + "\nActual: " + r.getActual())
          .collect(Collectors.joining("\n\n"));
      if (StringUtils.isNotBlank(result.getComment())) {
        message = result.getComment() + "\n" + message;
      }
      throw new AssertionError(message);
    }
  }
}
