package com.example.chess.game.session;

import com.example.chess.dto.board.MoveDto;
import com.example.chess.dto.message.CommandMessageDto;
import com.example.chess.dto.message.ErrorMessageDto;
import com.example.chess.dto.message.MessageDto;
import com.example.chess.dto.message.MoveMessageDto;
import com.example.chess.game.board.Move;
import com.example.chess.game.command.Command;
import com.example.chess.game.player.Player;
import org.springframework.context.ApplicationContext;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public abstract class GameSession {

    private final String id;
    private final Validator validator;
    private final GameParameters gameParameters;
    private final Instant expires;
    private volatile Player player;

    public GameSession(ApplicationContext applicationContext, GameParameters gameParameters, Duration timeout) {
        UUID id = UUID.randomUUID();
        this.id = id.toString();
        this.validator = applicationContext.getBean(Validator.class);
        this.gameParameters = gameParameters;
        this.expires = Instant.now().plus(timeout);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getId() {
        return id;
    }

    public GameParameters getParameters() {
        return gameParameters;
    }

    public boolean isInGame() {
        return player != null;
    }

    public boolean isExpired() {
        return !isActive() && expires.isBefore(Instant.now());
    }

    public abstract boolean isActive();

    public abstract void send(MessageDto messageDto);

    public void sendError(String message) {
        MessageDto errorMessageDto = new ErrorMessageDto(message);
        send(errorMessageDto);
    }

    public void receive(MessageDto messageDto) {
        if (isInGame()) {
            if (messageDto instanceof MoveMessageDto) {
                MoveMessageDto moveMessageDto = (MoveMessageDto) messageDto;
                MoveDto moveDto = moveMessageDto.getMoveDto();
                Set<ConstraintViolation<MessageDto>> violations = validator.validate(messageDto);
                if (!violations.isEmpty()) {
                    sendError("Wrong move parameters.");
                    return;
                }
                Move move = moveDto.toEntity();
                player.move(move);
            } else if (messageDto instanceof CommandMessageDto) {
                CommandMessageDto commandMessageDto = (CommandMessageDto) messageDto;
                Command command = commandMessageDto.getCommand();
                player.take(command);
            } else {
                sendError("Not supported message type.");
            }
        } else {
            sendError("Player is not in game.");
        }
    }

    public void close() {
        if (isInGame()) {
            player.take(Command.LEAVE);
        }
    }

}
