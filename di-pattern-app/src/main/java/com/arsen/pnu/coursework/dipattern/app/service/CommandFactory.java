package com.arsen.pnu.coursework.dipattern.app.service;

import com.arsen.pnu.coursework.dipattern.app.command.Command;
import com.arsen.pnu.coursework.dipattern.library.annotation.Autowired;
import com.arsen.pnu.coursework.dipattern.library.annotation.Component;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class CommandFactory {

    private final List<Command> commands;

    public String execute(String command) {
        if(StringUtils.isBlank(command)) {
            return "Empty command";
        }

        String[] splitted = command.split(" ");
        String prefix = splitted[0];
        Command commandInstance = getCommand(prefix);
        if(Objects.isNull(commandInstance)){
            return "Unknown command";
        }

        String[] args = Arrays.copyOfRange(splitted, 1, splitted.length);
        return commandInstance.execute(args);
    }

    private Command getCommand(String prefix) {
        return commands.stream().filter(command -> command.getPrefix().equals(prefix))
                .findFirst()
                .orElse(null);
    }
}
