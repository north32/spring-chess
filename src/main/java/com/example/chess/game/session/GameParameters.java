package com.example.chess.game.session;

import com.example.chess.game.Mode;
import com.example.chess.game.Side;

import java.util.Optional;

public class GameParameters {

    private final Mode mode;
    private final Side side;
    private final Optional<String> userIdO;
    private final Optional<String> opponentSessionIdO;

    public GameParameters(
            Mode mode,
            Side side,
            Optional<String> userIdO,
            Optional<String> opponentSessionIdO
    ) {
        this.mode = mode;
        this.side = side;
        this.userIdO = userIdO;
        this.opponentSessionIdO = opponentSessionIdO;
    }

    public Mode getMode() {
        return mode;
    }

    public Side getSide() {
        return side;
    }

    public Optional<String> getUserIdO() {
        return userIdO;
    }

    public Optional<String> getOpponentSessionIdO() {
        return opponentSessionIdO;
    }

    public static Builder builder(Mode mode) {
        return new Builder(mode);
    }

    public static GameParameters updateSide(GameParameters gameParameters, Side side) {
        Builder builder = new Builder(gameParameters.mode)
                .side(side);
        gameParameters.userIdO.ifPresent(builder::userId);
        gameParameters.opponentSessionIdO.ifPresent(builder::opponentSessionId);
        return builder.build();

    }

    public static class Builder {
        private final Mode mode;
        private Side side = Side.WHITE;
        private Optional<String> userId = Optional.empty();
        private Optional<String> opponentSessionId = Optional.empty();

        public Builder(Mode mode) {
            this.mode = mode;
        }

        public Builder side(Side side) {
            this.side = side;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = Optional.of(userId);
            return this;
        }

        public Builder opponentSessionId(String opponentSessionId) {
            this.opponentSessionId = Optional.of(opponentSessionId);
            return this;
        }

        public GameParameters build() {
            return new GameParameters(mode, side, userId, opponentSessionId);
        }
     }
}
