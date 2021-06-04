package com.example.chess.game.session.web;

import com.example.chess.dto.message.MessageDto;
import com.example.chess.game.session.GameParameters;
import com.example.chess.game.session.GameSession;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ExceptionWebSocketHandlerDecorator;

import java.time.Duration;

public class WebGameSession extends GameSession {

    public static final long SESSION_TIMEOUT_M = 10;

    private final ObjectMapper objectMapper;
    private volatile WebSocketSession session;


    public WebGameSession(ApplicationContext applicationContext, GameParameters gameParameters) {
        super(applicationContext, gameParameters, Duration.ofMinutes(SESSION_TIMEOUT_M));
        this.objectMapper = applicationContext.getBean(ObjectMapper.class);
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    @Override
    public boolean isActive() {
        return session != null && session.isOpen();
    }

    public void receive(String message) throws JsonProcessingException {
        // ExceptionWebSocketHandlerDecorator
        MessageDto messageDto = objectMapper.readValue(message, MessageDto.class);
        receive(messageDto);
    }

    @Override
    public void send(MessageDto messageDto) {
        if (isActive()) {
            try {
                String value = objectMapper.writeValueAsString(messageDto);
                TextMessage message = new TextMessage(value);
                session.sendMessage(message);
            } catch (Exception exception) {
                close();
            }
        }
    }
}
