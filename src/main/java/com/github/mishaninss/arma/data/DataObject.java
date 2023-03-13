/*
 * Copyright (c) 2021 Sergey Mishanin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.mishaninss.arma.data;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

public class DataObject {

  private static final String EXCEPTION_EMPTY_DATA_KEY = "Data Key cannot be null or blank string";
  public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  private static final Logger LOGGER = LoggerFactory.getLogger(DataObject.class);
  private static final Map<String, Supplier<String>> valueGenerators = new HashMap<>();

  private transient Map<String, String> valuesMap;

  public static void addValueGenerator(String key, Supplier<String> generator) {
    valueGenerators.put(key, generator);
  }

  public DataObject() {
  }

  public DataObject(Map<String, String> data) {
    fromMap(data);
  }

  private void generateValuesMap() {
    valuesMap = new HashMap<>();
    valueGenerators.forEach((key, generator) -> valuesMap.put(key, generator.get()));
  }

  public String resolveString(String value) {
    if (valuesMap == null) {
      generateValuesMap();
    }
    StringSubstitutor sub = new StringSubstitutor(valuesMap);
    return sub.replace(value);
  }

  public void fromMap(Map<String, String> data) {
    data.forEach(this::setProperty);
  }

  public Map<String, String> toMap() {
    List<String> dataKeys =
        Arrays.stream(FieldUtils.getFieldsWithAnnotation(getClass(), DataKey.class))
            .map(field -> field.getAnnotation(DataKey.class).value())
            .collect(Collectors.toList());
    Map<String, String> map = new HashMap<>();
    for (String dataKey : dataKeys) {
      String value = getProperty(dataKey);
      if (value != null) {
        map.put(dataKey, value);
      }
    }
    return map;
  }

  public void merge(DataObject another) {
    fromMap(another.toMap());
  }

  private void setProperty(String property, String value) {
    value = resolveString(value);

    Class<?> clazz = this.getClass();
    try {
      Field field = findPropertyField(clazz, property);
      if (field != null) {
        Method setter = findSetter(clazz, field.getName());
        if (setter != null) {
          MethodUtils.invokeMethod(this, true, setter.getName(), value);
        } else {
          if (field.getType() == String.class) {
            FieldUtils.writeField(field, this, value, true);
          }
        }
      }
    } catch (Exception ex) {
      LOGGER.trace("Could not set {} property to {} data object", property, this.getClass());
    }
  }

  private String getProperty(String property) {
    Class<?> clazz = this.getClass();
    try {
      Field field = findPropertyField(clazz, property);
      if (field != null) {
        Method getter = findGetter(clazz, field.getName());
        if (getter != null) {
          return objectToStringOrNull(MethodUtils.invokeMethod(this, true, getter.getName()));
        } else {
          return objectToStringOrNull(FieldUtils.readField(field, this, true));
        }
      }
    } catch (Exception ex) {
      LOGGER.trace("Could not get {} property of {} data object", property, this.getClass());
    }
    return null;
  }

  private String objectToStringOrNull(Object obj) {
    return obj == null ? null : String.valueOf(obj);
  }

  private Method findSetter(Class<?> clazz, String property) {
    Method[] methods = clazz.getMethods();
    String setterName = sanitizeElementId("set" + StringUtils.capitalize(property));
    for (Method method : methods) {
      if (StringUtils.equals(setterName, sanitizeElementId(method.getName()))
          && method.getParameterCount() == 1
          && method.getParameterTypes()[0] == String.class) {
        return method;
      }
    }
    return null;
  }

  private Method findGetter(Class<?> clazz, String property) {
    Method[] methods = clazz.getMethods();
    String getterName = sanitizeElementId("get" + property);
    for (Method method : methods) {
      if (StringUtils.equals(getterName, sanitizeElementId(method.getName()))
          && method.getParameterCount() == 0
          && method.getReturnType() == String.class) {
        return method;
      }
    }
    return null;
  }

  private Field findPropertyField(Class<?> clazz, String dataKey) {
    String propertyName = sanitizeElementId(dataKey);
    Field[] fields = FieldUtils.getFieldsWithAnnotation(clazz, DataKey.class);
    for (Field field : fields) {
      String currentDataKey = field.getAnnotation(DataKey.class).value();
      if (StringUtils.equals(sanitizeElementId(currentDataKey), propertyName)) {
        return field;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return GSON.toJson(this);
  }

  public static @NonNull
  String sanitizeElementId(@NonNull String elementId) {
    Preconditions.checkArgument(StringUtils.isNotBlank(elementId), EXCEPTION_EMPTY_DATA_KEY);
    elementId = StringUtils.normalizeSpace(elementId.replace("_", " "));
    if (elementId.contains(" ")) {
      elementId = elementId.toLowerCase().replace(" ", "_");
    } else {
      elementId = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(elementId), "_")
          .toLowerCase();
    }
    return elementId;
  }

  public static @NonNull <V> Map<String, V> sanitizeKeys(@NonNull Map<String, V> map) {
    Preconditions.checkNotNull(map);
    return map.entrySet().stream()
        .collect(Collectors.toMap(
            entry -> sanitizeElementId(entry.getKey()),
            Entry::getValue
        ));
  }

  public static @NonNull <V> List<Map<String, V>> sanitizeKeys(@NonNull List<Map<String, V>> list) {
    Preconditions.checkNotNull(list);
    return list.stream()
        .map(DataObject::sanitizeKeys)
        .collect(Collectors.toList());
  }

  public static List<String> splitAndNormalize(String value) {
    return splitAndNormalize(value, ",");
  }

  public static List<String> splitAndNormalize(String value, String delimiter) {
    return Arrays.stream(value.split(delimiter)).map(StringUtils::normalizeSpace)
        .collect(Collectors.toList());
  }
}
