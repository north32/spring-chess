package com.example.chess.dto.message;

public class ErrorMessageDto extends MessageDto {

    private final String message;

    public ErrorMessageDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
