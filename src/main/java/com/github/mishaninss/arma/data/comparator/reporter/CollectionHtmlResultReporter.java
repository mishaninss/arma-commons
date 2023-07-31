package com.github.mishaninss.arma.data.comparator.reporter;

import com.github.mishaninss.arma.data.comparator.CollectionComparisonResult;
import com.github.mishaninss.arma.data.comparator.ComparisonStatus;
import com.github.mishaninss.arma.data.comparator.ObjectComparisonResult;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

public class CollectionHtmlResultReporter<T> implements CollectionResultReporter<T> {

  private String path;
  private File file;

  public CollectionHtmlResultReporter(String path) {
    this.path = path;
  }

  @Override
  public void report(CollectionComparisonResult<T> result) {
    StringBuilder html = new StringBuilder();
    html.append("<html><head>");
    html.append("<style>"
        + ".table {border: 1px solid}"
        + ".pass {color: green}"
        + ".fail {color: red}"
        + ".show {background-color: yellow}"
        + ".hidden {display: none}");
    html.append("</style>");
    html.append("</head>");
    html.append("<body>");
    html.append(buildScripts());
    if (CollectionUtils.isNotEmpty(result.getActual())) {
      html.append(
          "<input type='checkbox' id='showErrorsOnly' onchange='toggleShowErrors(event)'><label for='showErrorsOnly'>Show mismatches only</label>");
      html.append("<br/>");
      html.append("Actual");
      html.append("<table class='table'>");
      html.append(buildTableHeader(result.getActual().get(0).getClass()));
      html.append("<tbody>");
      for (int i = 0; i < result.getActual().size(); i++) {
        var o = result.getActual().get(i);
        var oResult = result.getResults().get(i + 1);
        html.append(buildTableRow(i, o, oResult));
      }
      html.append("</tbody>");
      html.append("</table>");
    }

    html.append("<br/>");
    if (CollectionUtils.isNotEmpty(result.getExpected())) {
      html.append("Expected");
      html.append("<table class='table'>");
      html.append(buildTableHeader(result.getExpected().get(0).getClass()));
      html.append("<tbody>");
      for (int i = 0; i < result.getExpected().size(); i++) {
        var o = result.getExpected().get(i);
        var oResult = result.getResults().get(i + 1);
        html.append(buildTableRow(i, o, oResult));
      }
      html.append("</tbody>");
      html.append("</table>");
    }

    html.append("<br/>");
    if (MapUtils.isNotEmpty(result.getResults())) {
      html.append("Errors");
      html.append("<table class='table'>");
      html.append("<thead>");
      html.append("<tr><th>Row</th><th>Field</th><th>Error</th>");
      html.append("</thead>");
      html.append("<tbody>");
      result.getResults().keySet().stream()
          .sorted().forEach(i -> {
            var oResult = result.getResults().get(i);
            oResult.getResults().stream().filter(r -> ComparisonStatus.FAIL.equals(r.getStatus()))
                .forEach(r -> {
                  html.append("<tr class='row row_" + i + "' onclick='showRow(" + i + ")'>");
                  html.append("<td id='" + i + "_" + r.getField() + "'>" + i + "</td>");
                  html.append("<td>" + r.getField() + "</td>");
                  html.append(
                      "<td>" + r.getСomment() + "<br/>Expected: " + r.getExpected() + "<br/>Actual: "
                          + r.getActual() + "</td>");
                  html.append("</tr>");
                });
          });
      html.append("</tbody>");
      html.append("</table>");
    }

    html.append("</body>");
    html.append("</html>");

    String fileName = "report_" + new Date().getTime() + ".html";
    file = new File(path + "/" + fileName);
    try {
      FileUtils.write(file, html.toString(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    if (ComparisonStatus.FAIL.equals(result.getStatus())) {
      throw new AssertionError(
          "Comparison errors were found. See " + file.getAbsolutePath() + " for details");
    }
  }

  private String buildTableHeader(Class<?> clazz) {
    String header = "<thead><tr><th>#</th>";
    var fields = FieldUtils.getAllFieldsList(clazz);
    header += fields.stream()
        .map(f -> "<th>" + f.getName() + "</th>")
        .collect(Collectors.joining());
    header += "</tr></thead>";
    return header;
  }

  private String buildTableRow(int i, Object obj, ObjectComparisonResult result) {
    int rowIndex = i + 1;
    String trClass = "row row_" + rowIndex;
    if (ComparisonStatus.PASS.equals(result.getStatus())) {
      trClass += " trPass";
    } else {
      trClass += " trFail";
    }
    String header =
        "<tr class='" + trClass + "' onclick='showRow(" + rowIndex
            + ")'><td><input type='checkbox' index='" + rowIndex
            + "' onchange='toggleShowSelected(event)'>" + rowIndex + "</td>";
    var fields = FieldUtils.getAllFieldsList(obj.getClass());
    header += fields.stream()
        .map(f -> {
          try {
            String tdClass;
            var fResult = result.getFiledResult(f.getName());
            if (fResult == null) {
              tdClass = "nocheck";
            } else if (ComparisonStatus.PASS.equals(fResult.getStatus())) {
              tdClass = "pass";
            } else {
              tdClass = "fail";
            }
            return "<td class='" + tdClass + "'>" + FieldUtils.readField(f, obj, true) + "</td>";
          } catch (Exception ex) {
            throw new RuntimeException(ex);
          }
        })
        .collect(Collectors.joining());
    header += "</tr>";
    return header;
  }

  private String buildScripts() {
    return "<script type = 'text/javascript'>"
        + "function showRow(index) {"
        + "Array.from(document.getElementsByClassName('show')).forEach(el => el.classList.remove('show'));"
        + "Array.from(document.getElementsByClassName('row_' + index)).forEach(el => el.classList.remove('hidden'));"
        + "Array.from(document.getElementsByClassName('row_' + index)).forEach(el => el.classList.add('show'));"
        + "}"
        + "function toggleShowErrors(event) {"
        + "    var checkbox = event.target;"
        + "    if (checkbox.checked) {"
        + "     Array.from(document.getElementsByClassName('trPass')).forEach(el => el.classList.add('hidden'));"
        + "    } else {"
        + "        Array.from(document.getElementsByClassName('trPass')).forEach(el => el.classList.remove('hidden'));"
        + "    }"
        + "}"
        + "function toggleShowSelected(event) {"
        + "    var checkbox = event.target;"
        + "    if (checkbox.checked) {"
        + "     Array.from(document.getElementsByClassName('row')).forEach(el => el.classList.add('hidden'));"
        + "     showRow(checkbox.getAttribute('index'));"
        + "    } else {"
        + "        Array.from(document.getElementsByClassName('row')).forEach(el => el.classList.remove('hidden'));"
        + "    }"
        + "}"
        + "</script>";
  }
}
