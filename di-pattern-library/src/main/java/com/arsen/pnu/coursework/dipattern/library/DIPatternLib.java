package com.arsen.pnu.coursework.dipattern.library;

import com.arsen.pnu.coursework.dipattern.library.context.ApplicationContext;
import lombok.Getter;

@Getter
public class DIPatternLib {

    private final ApplicationContext context;

    private DIPatternLib(Class<?> appClass) {
        this.context = new ApplicationContext(appClass);
    }

    public static void start(Class<?> appClass, String[] args) {
        new DIPatternLib(appClass);
    }
}
