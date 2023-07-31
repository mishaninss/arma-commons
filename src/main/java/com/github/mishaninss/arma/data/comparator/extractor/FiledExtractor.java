package com.github.mishaninss.arma.data.comparator.extractor;

import org.apache.commons.lang3.reflect.FieldUtils;

public class FiledExtractor implements Extractor{

  @Override
  public Object extract(Object object, String fieldName) {
    try {
      return FieldUtils.readField(object, fieldName, true);
    } catch (IllegalAccessException ex){
      throw new RuntimeException(ex);
    }
  }
}
