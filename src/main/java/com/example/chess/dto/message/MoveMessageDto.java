package com.example.chess.dto.message;

import com.example.chess.dto.board.MoveDto;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MoveMessageDto extends MessageDto {

    private final MoveDto moveDto;

    public MoveMessageDto(
            @JsonProperty("move") MoveDto moveDto
    ) {
        this.moveDto = moveDto;
    }

    public MoveDto getMoveDto() {
        return moveDto;
    }
}
