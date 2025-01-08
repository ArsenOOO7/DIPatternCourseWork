package com.arsen.pnu.coursework.dipattern.app.command;

public interface Command {

    String getPrefix();

    String execute(String[] args);

}
