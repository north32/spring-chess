package com.example.chess.dto.message;

import com.example.chess.game.command.Command;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CommandMessageDto extends MessageDto {

    private final Command command;

    public CommandMessageDto(
            @JsonProperty("command") Command command
    ) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }
}
