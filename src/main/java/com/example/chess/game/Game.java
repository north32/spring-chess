package com.example.chess.game;

import com.example.chess.domain.game.GameResult;
import com.example.chess.game.board.Board;
import com.example.chess.game.board.Move;
import com.example.chess.game.board.MoveResult;
import com.example.chess.game.command.CommandResult;
import com.example.chess.game.exceptions.GameException;
import com.example.chess.game.player.Player;
import com.example.chess.game.player.UserPlayer;
import com.example.chess.service.GameResultService;

import java.util.EnumMap;
import java.util.List;

public class Game {

    private final GameResultService gameResultService;
    private final Mode mode;
    private final EnumMap<Side, Player> players;
    private final Board board;
    private volatile State state;

    public Game(GameResultService gameResultService, Player player1, Player player2, Mode mode, Board board) {
        this.gameResultService = gameResultService;
        this.mode = mode;
        this.players = new EnumMap<>(Side.class);
        players.put(player1.getSide(), player1);
        players.put(player2.getSide(), player2);
        if (player1.getSide() == player2.getSide()) {
            throw new GameException("Players can't be on the same side.");
        }
        this.board = board;
        this.state = State.ONGOING;
    }

    public synchronized List<Move> getLegalMoves() {
        return board.getLegalMoves();
    }

    public synchronized State getState() {
        return state;
    }

    public synchronized MoveResult makeMove(Player player, Move move) {
        if (state == State.ONGOING) {
            if (player.getSide() == board.getMoveSide()) {
                if (board.isMoveLegal(move)) {
                    board.move(move);
                    state = board.getState();
                    if (state != State.ONGOING) {
                        getOppositePlayer(player).notifyStateChanged(state);
                        result();
                        return MoveResult.STATE_CHANGED;
                    } else {
                        getOppositePlayer(player).notifyMove(move);
                        return MoveResult.SUCCESS;
                    }
                } else {
                    return MoveResult.ILLEGAL_MOVE;
                }
            } else {
                return MoveResult.NOT_YOUR_TURN;
            }
        } else{
            return MoveResult.ILLEGAL_STATE;
        }
    }

    public synchronized CommandResult leave(Player player) {
        if (state == State.ONGOING) {
            Player opposite = getOppositePlayer(player);
            state = opposite.getSide() == Side.WHITE? State.WHITE_WON : State.BLACK_WON;
            opposite.notifyStateChanged(state);
            result();
            return CommandResult.SUCCESS;
        } else {
            return CommandResult.FAIL;
        }
    }

    private void result() {
        GameResult.Builder builder = new GameResult.Builder(mode, state);
        if (players.get(Side.WHITE) instanceof UserPlayer) {
            UserPlayer player = (UserPlayer) players.get(Side.WHITE);
            builder.whitePlayerUserId(player.getUserId());
        }
        if (players.get(Side.BLACK) instanceof UserPlayer) {
            UserPlayer player = (UserPlayer) players.get(Side.BLACK);
            builder.blackPlayerUserId(player.getUserId());
        }
        builder.moveList(board.getMoveHistory());
        gameResultService.saveGameResult(builder.build());
    }

    private Player getOppositePlayer(Player player) {
        return players.get(player.getSide().opposite());
    }

}
