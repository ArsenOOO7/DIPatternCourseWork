package com.arsen.pnu.coursework.dipattern.app.service;

import com.arsen.pnu.coursework.dipattern.library.annotation.Autowired;
import com.arsen.pnu.coursework.dipattern.library.annotation.Component;

import java.util.Scanner;

@Component
public class CommandService {

    @Autowired
    private CommandFactory factory;

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter command: ");
            String line = scanner.nextLine();
            String response = factory.execute(line);
            System.out.println(response);
        }
    }
}
