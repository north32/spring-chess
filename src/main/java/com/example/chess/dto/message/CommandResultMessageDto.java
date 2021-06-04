package com.example.chess.dto.message;

import com.example.chess.game.command.CommandResult;

public class CommandResultMessageDto extends MessageDto {

    private final CommandResult commandResult;


    public CommandResultMessageDto(CommandResult commandResult) {
        this.commandResult = commandResult;
    }

    public CommandResult getCommandResult() {
        return commandResult;
    }
}
