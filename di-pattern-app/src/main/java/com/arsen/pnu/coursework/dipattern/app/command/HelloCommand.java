package com.arsen.pnu.coursework.dipattern.app.command;

import com.arsen.pnu.coursework.dipattern.library.annotation.Component;

@Component
public class HelloCommand implements Command {

    @Override
    public String getPrefix() {
        return "/hello";
    }

    @Override
    public String execute(String[] args) {
        if(args.length != 1) {
            return "Usage: /hello <name>";
        }
        return "Hello, %s.".formatted(args[0]);
    }
}
