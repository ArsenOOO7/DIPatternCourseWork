package com.arsen.pnu.coursework.dipattern.library.exception;

public class DuplicateBeanException extends RuntimeException {

    public DuplicateBeanException(Class<?> type, String name) {
        super("Duplicate bean found: " + type + " " + name);
    }
}
