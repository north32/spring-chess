package com.example.chess.game.player;

import com.example.chess.dto.board.MoveDto;
import com.example.chess.dto.message.CommandResultMessageDto;
import com.example.chess.dto.message.MessageDto;
import com.example.chess.dto.message.MoveMessageDto;
import com.example.chess.dto.message.StateMessageDto;
import com.example.chess.game.Side;
import com.example.chess.game.State;
import com.example.chess.game.board.Move;
import com.example.chess.game.board.MoveResult;
import com.example.chess.game.command.Command;
import com.example.chess.game.command.CommandResult;
import com.example.chess.game.session.GameSession;

public class SessionPlayer extends Player {

    private final GameSession session;

    public SessionPlayer(Side side, GameSession session) {
        super(side);
        this.session = session;
    }

    @Override
    public void notifyMove(Move move) {
        MoveDto moveDto = MoveDto.toDto(move);
        MessageDto messageDto = new MoveMessageDto(moveDto);
        session.send(messageDto);
    }

    @Override
    public void notifyStateChanged(State state) {
        MessageDto messageDto = new StateMessageDto(state);
        session.send(messageDto);
    }

    @Override
    public MoveResult move(Move move) {
        MoveResult moveResult = super.move(move);
        if (moveResult == MoveResult.STATE_CHANGED) {
            State state = game.getState();
            notifyStateChanged(state);
        }
        if (moveResult != MoveResult.SUCCESS && moveResult != MoveResult.STATE_CHANGED) {
            String message = moveResult.name().toLowerCase();
            session.sendError(message);
        }
        return moveResult;
    }

    @Override
    public CommandResult take(Command command) {
        CommandResult commandResult = super.take(command);
        MessageDto messageDto = new CommandResultMessageDto(commandResult);
        session.send(messageDto);
        return commandResult;
    }
}
