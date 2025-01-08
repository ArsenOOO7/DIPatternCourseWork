package com.arsen.pnu.coursework.dipattern.app.service;

import com.arsen.pnu.coursework.dipattern.app.command.Command;
import com.arsen.pnu.coursework.dipattern.library.annotation.Component;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CommandFactory {

    private final List<Command> commands;

    public String execute(String value) {
        return Optional.ofNullable(value)
                .filter(StringUtils::isNotBlank)
                .map(command -> command.split(" "))
                .map(arguments -> getCommand(arguments[0])
                        .map(command -> command.execute(Arrays.copyOfRange(arguments, 1, arguments.length)))
                        .orElse("Unknown command"))
                .orElse("Empty command");
    }

    private Optional<Command> getCommand(String prefix) {
        return commands.stream().filter(command -> command.getPrefix().equals(prefix))
                .findFirst();
    }
}
