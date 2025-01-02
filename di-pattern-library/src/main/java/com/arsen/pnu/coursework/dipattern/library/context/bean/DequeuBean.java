package com.arsen.pnu.coursework.dipattern.library.context.bean;

import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.Constructor;

@Getter
@Builder
public class DequeuBean {

    private String name;
    private Class<?> type;
    private Constructor<?> constructor;

}
