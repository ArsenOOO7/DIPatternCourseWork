package com.arsen.pnu.coursework.dipattern.app;

import com.arsen.pnu.coursework.dipattern.app.component.AutowiredTestBean;
import com.arsen.pnu.coursework.dipattern.library.DIPatternLib;
import com.arsen.pnu.coursework.dipattern.library.context.ApplicationContext;

public class Application {

    public static void main(String[] args) {
        ApplicationContext context = DIPatternLib.start(Application.class, args);
        AutowiredTestBean autowiredTestBean = context.getBean(AutowiredTestBean.class);
        System.out.println(autowiredTestBean.getTestComponent().getValue());
    }
}