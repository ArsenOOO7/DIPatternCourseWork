package com.arsen.pnu.coursework.dipattern.app.command;

import com.arsen.pnu.coursework.dipattern.library.annotation.Component;

@Component
public class ByeCommand implements Command {

    @Override
    public String getPrefix() {
        return "/bye";
    }

    @Override
    public String execute(String[] args) {
        if(args.length != 1) {
            return "Usage: /hello <name>";
        }
        return "Bye, %s.".formatted(args[0]);
    }
}
