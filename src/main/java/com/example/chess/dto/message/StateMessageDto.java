package com.example.chess.dto.message;

import com.example.chess.game.State;

public class StateMessageDto extends MessageDto {

    private final State state;

    public StateMessageDto(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }
}
