package com.example.chess.game.impl.board;

import com.example.chess.game.Side;
import com.example.chess.game.State;
import com.example.chess.game.board.Board;
import com.example.chess.game.board.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChessLibBoard implements Board {

    private final com.github.bhlangonijr.chesslib.Board board;
    private final List<Move> moveHistory;

    public ChessLibBoard() {
        this.board = new com.github.bhlangonijr.chesslib.Board();
        moveHistory = new ArrayList<>();
    }

    @Override
    public List<Move> getLegalMoves() {
        return board.legalMoves()
                .stream()
                .map(move -> new Move(move.getFrom().toString(), move.getTo().toString(), Optional.empty()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isMoveLegal(Move move) {
        return getLegalMoves().contains(move);
    }

    @Override
    public void move(Move move) {
        com.github.bhlangonijr.chesslib.move.Move chessLibMove = new com.github.bhlangonijr.chesslib.move.Move(
                move.getFrom() + move.getTo() + move.getPromotionO().orElse("q"), board.getSideToMove()
        );
        board.doMove(chessLibMove);
        moveHistory.add(move);
    }

    @Override
    public State getState() {
        if (board.isDraw() || board.isStaleMate() || board.isRepetition()) {
            return State.DRAW;
        }
        boolean isMated = board.isMated();
        if (isMated) {
            return getMoveSide() == Side.WHITE ? State.WHITE_WON : State.BLACK_WON;
        }
        return State.ONGOING;
    }

    @Override
    public Side getMoveSide() {
        return board.getSideToMove() == com.github.bhlangonijr.chesslib.Side.WHITE? Side.WHITE : Side.BLACK;
    }

    @Override
    public List<Move> getMoveHistory() {
        return moveHistory;
    }

    public com.github.bhlangonijr.chesslib.Board getChessLibBoard() {
        return board;
    }
}
