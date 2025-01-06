package com.arsen.pnu.coursework.dipattern.library.context.bean;

import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;

@Getter
@Builder
public class InternalBean {

    private String name;
    private Class<?> type;
    private Constructor<?> constructor;
    private Map<Field, Class<?>> autowiredTypesMap;

}
