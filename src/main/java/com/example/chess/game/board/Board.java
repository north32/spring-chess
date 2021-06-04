package com.example.chess.game.board;

import com.example.chess.game.Side;
import com.example.chess.game.State;

import java.util.List;

public interface Board {

    List<Move> getLegalMoves();

    boolean isMoveLegal(Move move);

    void move(Move move);

    State getState();

    Side getMoveSide();

    List<Move> getMoveHistory();

}
