package com.arsen.pnu.coursework.dipattern.library.exception;

public class BeanNotFoundException extends RuntimeException {

    public BeanNotFoundException(Class<?> type) {
        super("Bean with type " + type.getName() + " not found");
    }
}