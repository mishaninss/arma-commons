package com.github.mishaninss.arma.data.comparator.extractor;

import java.lang.reflect.Method;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

public class GetterExtractor implements Extractor {

  @Override
  public Object extract(Object object, String fieldName) {
    try {
      return findGetter(object.getClass(), fieldName).invoke(object);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Method findGetter(Class<?> clazz, String fieldName) {
    String getterName = "get" + StringUtils.capitalize(fieldName);
    Method getter = MethodUtils.getAccessibleMethod(clazz, getterName);
    if (getter == null) {
      throw new IllegalArgumentException(
          "Could not find [" + getterName + "] method for class [" + clazz.getCanonicalName()
              + "]");
    }
    return getter;
  }
}