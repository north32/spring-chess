package com.example.chess.game.player;

import com.example.chess.game.Side;
import com.example.chess.game.session.GameSession;

public class UserPlayer extends SessionPlayer {

    private final String userId;

    public UserPlayer(Side side, GameSession session, String userId) {
        super(side, session);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
