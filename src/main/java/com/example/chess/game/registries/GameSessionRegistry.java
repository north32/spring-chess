package com.example.chess.game.registries;

import com.example.chess.game.session.GameSession;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class GameSessionRegistry {

    private final Map<String, GameSession> sessions = new HashMap<>();

    public synchronized void add(GameSession session) {
        sessions.put(session.getId(), session);
    }

    public synchronized Optional<GameSession> getById(String id) {
        return Optional.ofNullable(sessions.get(id));
    }

    public synchronized void remove(GameSession session) {
        sessions.remove(session.getId());
    }

    public synchronized void clear() {
       sessions.entrySet().removeIf(e -> e.getValue().isExpired());
    }

}
