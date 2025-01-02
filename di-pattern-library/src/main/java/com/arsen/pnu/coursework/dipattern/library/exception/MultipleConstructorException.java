package com.arsen.pnu.coursework.dipattern.library.exception;

public class MultipleConstructorException extends RuntimeException{

    public MultipleConstructorException(Class<?> type) {
        super("Found multiple constructors of type " + type.getName());
    }

}
