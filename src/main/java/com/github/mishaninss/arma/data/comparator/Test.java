package com.github.mishaninss.arma.data.comparator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mishaninss.arma.data.CsvDataExtractor;
import com.github.mishaninss.arma.data.comparator.checker.CollectionEqualsChecker;
import com.github.mishaninss.arma.data.comparator.checker.EqualsIgnoreCaseChecker;
import com.github.mishaninss.arma.data.comparator.reporter.CollectionHtmlResultReporter;
import java.util.List;
import java.util.function.BiPredicate;

public class Test {


  public static void main(String[] args) {
    var listX = CsvDataExtractor.extractDataAsListOfObjects("/Users/ssmishanin/arma/arma-commons/src/main/resources/baseline.csv", Entity.class);
    var listY = CsvDataExtractor.extractDataAsListOfObjects("/Users/ssmishanin/arma/arma-commons/src/main/resources/baseline_err.csv", Entity.class);
    CsvDataExtractor.saveListOfObjectsToCsvFile("out.csv",listX);

    var checker = new ObjectChecker()
        .as("Entity")
        .forClass(Entity.class)
        .forFieldType(String.class, new EqualsIgnoreCaseChecker())
        .forField("name", (ex, ac) -> ex.equals(ac), "sdfsdf")
        .forField("isActive", true)
        .excluding("id");

    new CollectionChecker<Entity>()
        .withChecker(new CollectionEqualsChecker(checker))
        .compare(listX, listY)
        .report();

  }

  public static class Entity {

    private String name;
    private Long id;
    @JsonProperty("isActive")
    private Boolean isActive;

    public String getName() {
      return name;
    }

    public Entity setName(String name) {
      this.name = name;
      return this;
    }

    public Long getId() {
      return id;
    }

    public Entity setId(Long id) {
      this.id = id;
      return this;
    }

    public Boolean getActive() {
      return isActive;
    }

    public Entity setActive(Boolean active) {
      isActive = active;
      return this;
    }
  }

}
