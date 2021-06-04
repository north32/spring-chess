package com.example.chess.game.impl.player;

import com.example.chess.game.Side;
import com.example.chess.game.State;
import com.example.chess.game.board.Move;
import com.example.chess.game.player.BotPlayer;
import com.github.bhlangonijr.kengine.SearchEngine;
import com.github.bhlangonijr.kengine.SearchState;

import java.util.Optional;

public class ChessLibBot extends BotPlayer {

    private SearchEngine engine;
    private SearchState state;

    public ChessLibBot(Side side, SearchEngine engine, SearchState state) {
        super(side);
        this.engine = engine;
        this.state = state;
    }

    @Override
    public void notifyMove(Move move) {
        com.github.bhlangonijr.chesslib.move.Move chessLibMove = engine.rooSearch(state);
        move(new Move(
                        chessLibMove.getFrom().toString(),
                        chessLibMove.getTo().toString(),
                        Optional.of(chessLibMove.getPromotion().toString())
                )
        );
    }

    @Override
    public void notifyStateChanged(State state) {
        // Empty because we don't need to free resources after game end.
    }
}
