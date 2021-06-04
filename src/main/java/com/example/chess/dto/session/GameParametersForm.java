package com.example.chess.dto.session;

import com.example.chess.game.Mode;
import com.example.chess.game.Side;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public class GameParametersForm {

    private Mode mode;
    private Optional<Side> sideO;
    private Optional<String> opponentSessionIdO;

    @JsonCreator
    public GameParametersForm(
            @JsonProperty(value = "mode", required = true) Mode mode,
            @JsonProperty(value = "side") Optional<Side> sideO,
            @JsonProperty(value = "opponentSessionId") Optional<String> opponentSessionIdO
    ) {
        this.mode = mode;
        this.sideO = sideO;
        this.opponentSessionIdO = opponentSessionIdO;
    }

    public Mode getMode() {
        return mode;
    }

    public Optional<Side> getSideO() {
        return sideO;
    }

    public Optional<String> getOpponentSessionIdO() {
        return opponentSessionIdO;
    }

}
