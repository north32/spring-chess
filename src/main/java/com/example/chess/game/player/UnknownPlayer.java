package com.example.chess.game.player;

import com.example.chess.game.Side;
import com.example.chess.game.session.GameSession;

public class UnknownPlayer extends SessionPlayer {

    public UnknownPlayer(Side side, GameSession session) {
        super(side, session);
    }
}
