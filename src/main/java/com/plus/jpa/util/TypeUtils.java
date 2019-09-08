package com.plus.jpa.util;


import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Allen
 */
public class TypeUtils {

    public static Field getField(Class<?> clazz, String fieldName) {
        Class<?> originalClass = clazz;
        while (null != clazz) {
            Field[] fields = clazz.getDeclaredFields();
            Optional<Field> found = Arrays.stream(fields).filter(f -> f.getName().equals(fieldName)).findAny();
            if (found.isPresent()) {
                return found.get();
            } else {
                clazz = clazz.getSuperclass();
            }
        }
        throw new RuntimeException("类型不包含字段，Class：" + originalClass.getName() + "，Field：" + fieldName);
    }

    public static Type getFieldType(Class<?> clazz, String fieldName) {
        Field field = getField(clazz, fieldName);
        return field.getGenericType();
    }

    public static Type getGenericInnerType(ParameterizedType type) {
        return getGenericInnerType(type, 0);
    }

    public static Type getGenericInnerType(ParameterizedType type, int index) {
        return type.getActualTypeArguments()[index];
    }

}
