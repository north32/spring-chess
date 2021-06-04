package com.example.chess.registries;

import com.example.chess.domain.account.User;
import com.example.chess.domain.account.VerificationToken;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class VerificationTokenUserRegistry {

    private final Map<VerificationToken, User> users = new HashMap<>();

    public synchronized VerificationToken add(User user) {
        VerificationToken token = VerificationToken.create();
        users.put(token, user);
        return token;
    }

    public synchronized Optional<User> get(VerificationToken token) {
        return Optional.ofNullable(users.get(token));
    }

    public synchronized void remove(VerificationToken token) {
        users.remove(token);
    }

    public synchronized void clear() {
        users.entrySet().removeIf(e -> e.getKey().isExpired());
    }
}
