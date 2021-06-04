package com.example.chess.game.registries;

import com.example.chess.game.session.GameSession;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

@Component
public class GameSearchRegistry {

    private final Queue<GameSession> sessions = new LinkedList<>();

    public synchronized void add(GameSession session) {
        sessions.add(session);
    }

    public synchronized Optional<GameSession> getSession() {
        if (sessions.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(sessions.poll());
    }

    public synchronized void remove(GameSession session) {
        sessions.removeIf(s -> s.getId().equals(session.getId()));
    }

    public synchronized void clear() {
        sessions.removeIf(GameSession::isExpired);
    }

}
