package com.github.mishaninss.arma.data.comparator;

import com.github.mishaninss.arma.data.comparator.checker.CollectionEqualsChecker;
import com.github.mishaninss.arma.data.comparator.checker.EqualsIgnoreCaseChecker;
import com.github.mishaninss.arma.data.comparator.reporter.CollectionHtmlResultReporter;
import java.util.List;
import java.util.function.BiPredicate;

public class Test {


  public static void main(String[] args) {
    var a = new Entity()
        .setName("Test1")
        .setId(1L)
        .setActive(null);
    var b = new Entity()
        .setName("test2")
        .setId(2L)
        .setActive(false);

    var listA = List.of(a,a,a,a,a,a,a,a);
    var listB = List.of(a,b,b,a,a,b,b,a);

    var checker = new ObjectChecker()
        .as("Entity")
        .forClass(Entity.class)
        .forFieldType(String.class, new EqualsIgnoreCaseChecker())
        .forField("name", (ex, ac) -> ex == ac, "sdfsdf")
        .forField("isActive", true)
        .excluding("id")
        .compare(a, b);

    new CollectionChecker<Entity>()
        .withChecker(new CollectionEqualsChecker(checker))
        .compare(listA, listB)
        .report();

  }

  public static class Entity {

    private String name;
    private Long id;
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
