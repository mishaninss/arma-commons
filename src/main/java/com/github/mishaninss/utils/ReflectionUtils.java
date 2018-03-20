/*
 *
 * Copyright 2018 Sergey Mishanin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.mishaninss.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReflectionUtils {
    private ReflectionUtils(){}
    
    public static Class<?> getInterfaceGenericClass(Object obj, Class<?> genericInterface, int index){
        Type[] genericInterfaces = obj.getClass().getGenericInterfaces();
        for (Type genInterface: genericInterfaces){
            if (genInterface.getTypeName().startsWith(genericInterface.getTypeName())){
                Type tType = ((ParameterizedType)genInterface).getActualTypeArguments()[index];
                String className = tType.getTypeName();
                try {
                    return Class.forName(className);
                } catch (ClassNotFoundException ex) {
                    return null;
                }
            }
        }
        return null;
    }

    public static Class getGenericClass(Object obj, int index){
        return getGenericClass(obj.getClass(), index);
    }

    public static Class getGenericClass(Type type, int index){
        if (type instanceof ParameterizedType){
            Type[] typeArguments = ((ParameterizedType)type).getActualTypeArguments();
            if (typeArguments.length < index + 1) {
                return null;
            }
            return getClassByType(typeArguments[index]);
        }
        return null;
    }

    public static Class getGenericClass(Class clazz, int index){
        ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();

        if (parameterizedType == null){
            return null;
        }
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        if (typeArguments.length < index + 1){
            return null;
        }
        return getClassByType(typeArguments[index]);
    }
    
    public static Class getClassByType(Type type){
        try {
            return Class.forName(type.getTypeName());
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    public static List<Class> getInnerClassesWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass){
        return Arrays.stream(clazz.getDeclaredClasses())
                .filter(innerClass -> innerClass.isAnnotationPresent(annotationClass))
                .collect(Collectors.toList());
    }

    public static Object changeAnnotationValue(Annotation annotation, String key, Object newValue){
        Object handler = Proxy.getInvocationHandler(annotation);
        Field f;
        try {
            f = handler.getClass().getDeclaredField("memberValues");
        } catch (NoSuchFieldException | SecurityException e) {
            throw new IllegalStateException(e);
        }
        f.setAccessible(true);
        Map<String, Object> memberValues;
        try {
            memberValues = (Map<String, Object>) f.get(handler);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        Object oldValue = memberValues.get(key);
        if (oldValue == null || oldValue.getClass() != newValue.getClass()) {
            throw new IllegalArgumentException();
        }
        memberValues.put(key,newValue);
        return oldValue;
    }
}
