package com.example.chess.dto.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MoveMessageDto.class, name = "move"),
        @JsonSubTypes.Type(value = CommandMessageDto.class, name = "command"),
        @JsonSubTypes.Type(value = CommandResultMessageDto.class, name = "command_result"),
        @JsonSubTypes.Type(value = ErrorMessageDto.class, name = "error"),
        @JsonSubTypes.Type(value = StateMessageDto.class, name = "state")
})
public abstract class MessageDto { }
