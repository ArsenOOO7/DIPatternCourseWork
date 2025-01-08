package com.arsen.pnu.coursework.dipattern.app;

import com.arsen.pnu.coursework.dipattern.app.service.CommandService;
import com.arsen.pnu.coursework.dipattern.library.DIPatternLib;
import com.arsen.pnu.coursework.dipattern.library.context.ApplicationContext;

public class Application {

    public static void main(String[] args) {
        ApplicationContext context = DIPatternLib.start(Application.class, args);
        CommandService service = context.getBean(CommandService.class);
        service.run();
    }
}