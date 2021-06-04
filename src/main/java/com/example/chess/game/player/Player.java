package com.example.chess.game.player;

import com.example.chess.game.Game;
import com.example.chess.game.Side;
import com.example.chess.game.State;
import com.example.chess.game.board.Move;
import com.example.chess.game.board.MoveResult;
import com.example.chess.game.command.Command;
import com.example.chess.game.command.CommandResult;
import com.example.chess.game.exceptions.GameException;

public abstract class Player {

    private final Side side;
    protected volatile Game game;

    public Player(Side side) {
        this.side = side;
    }

    public void setGame(Game game) {
        if (this.game != null) {
            throw new GameException("Game already set.");
        }
        this.game = game;
    }

    public Side getSide() {
        return side;
    }

    public abstract void notifyMove(Move move);

    public abstract void notifyStateChanged(State state);

    public MoveResult move(Move move) {
        return game.makeMove(this, move);
    }

    public CommandResult take(Command command) {
        if (command == Command.LEAVE) {
            return game.leave(this);
        }
        throw new GameException("Unsupported command.");
    }

}
