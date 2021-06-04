package com.example.chess.game.session.web;

import com.example.chess.game.exceptions.GameSessionException;
import com.example.chess.game.registries.GameSessionRegistry;
import com.example.chess.game.session.GameSession;
import com.example.chess.service.GameService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.Optional;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    private final GameSessionRegistry gameSessionRegistry;
    private final GameService gameService;

    public GameWebSocketHandler(GameSessionRegistry gameSessionRegistry, GameService gameService) {
        this.gameSessionRegistry = gameSessionRegistry;
        this.gameService = gameService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session)  {
        WebGameSession gameSession = (WebGameSession) getGameSession(session);
        gameSession.setSession(session);
        gameService.sessionConnected(gameSession);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        WebGameSession gameSession = (WebGameSession) getGameSession(session);
        gameSession.receive(message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        GameSession gameSession = getGameSession(session);
        gameSession.close();
        gameSessionRegistry.remove(gameSession);
        gameService.sessionDisconnected(gameSession);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        // TODO: log exception.
        System.out.println(exception.getMessage());
    }

    private GameSession getGameSession(WebSocketSession session) {
        URI uri = session.getUri();
        if (uri == null) {
            throw new GameSessionException("Session not found!");
        }
        String path = uri.getPath();
        String id = lastPathSegment(path);
        Optional<GameSession> gameSessionO = gameSessionRegistry.getById(id);
        if (gameSessionO.isEmpty()) {
            throw new GameSessionException("Session not found!");
        }
        return gameSessionO.get();
    }

    private String lastPathSegment(String path) {
        return path.substring(path.lastIndexOf('/') + 1);
    }
}
