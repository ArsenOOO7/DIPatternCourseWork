package com.arsen.pnu.coursework.dipattern.library.util;

import com.arsen.pnu.coursework.dipattern.library.annotation.Autowired;
import com.arsen.pnu.coursework.dipattern.library.exception.MultipleConstructorException;
import lombok.experimental.UtilityClass;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class BeanUtil {

    public static Constructor<?> getConstructor(Class<?> type) {
        Constructor<?>[] constructors = type.getDeclaredConstructors();
        if (constructors.length > 1) {
            throw new MultipleConstructorException(type);
        }
        return makeAccessible(constructors[0]);
    }

    public static List<Class<?>> getParameterTypes(Constructor<?> constructor) {
        return Arrays.asList(constructor.getParameterTypes());
    }

    public static Map<Field, Class<?>> getAutowiredFieldTypesMap(Class<?> type) {
        Field[] fields = type.getDeclaredFields();
        if (fields.length == 0) {
            return Map.of();
        }
        Map<Field, Class<?>> map = new HashMap<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                map.put(makeAccessible(field), field.getType());
            }
        }
        return map;
    }

    public static <T extends AccessibleObject> T makeAccessible(T accessible) {
        accessible.setAccessible(true);
        return accessible;
    }

}
