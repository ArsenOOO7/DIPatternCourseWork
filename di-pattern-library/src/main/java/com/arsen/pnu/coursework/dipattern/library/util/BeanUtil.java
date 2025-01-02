package com.arsen.pnu.coursework.dipattern.library.util;

import com.arsen.pnu.coursework.dipattern.library.exception.MultipleConstructorException;
import lombok.experimental.UtilityClass;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

@UtilityClass
public class BeanUtil {

    public static Constructor<?> getConstructor(Class<?> type) {
        Constructor<?>[] constructors = type.getDeclaredConstructors();
        if(constructors.length > 1){
            throw new MultipleConstructorException(type);
        }
        return makeAccessible(constructors[0]);
    }

    public static List<Class<?>> getParameterTypes(Constructor<?> constructor) {
        return Arrays.asList(constructor.getParameterTypes());
    }

    public static <T extends AccessibleObject> T makeAccessible(T accessible) {
        accessible.setAccessible(true);
        return accessible;
    }

}
