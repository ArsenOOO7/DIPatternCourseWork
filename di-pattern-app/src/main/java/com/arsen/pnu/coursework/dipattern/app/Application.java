package com.arsen.pnu.coursework.dipattern.app;

import com.arsen.pnu.coursework.dipattern.app.component.TestComponent;
import com.arsen.pnu.coursework.dipattern.library.DIPatternLib;
import com.arsen.pnu.coursework.dipattern.library.context.ApplicationContext;

public class Application {

    public static void main(String[] args) {
        DIPatternLib.start(Application.class, args);
        ApplicationContext context = ApplicationContext.getInstance();
        TestComponent component = context.getBean(TestComponent.class);
        System.out.println(component.getValue());
    }
}