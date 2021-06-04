package com.example.chess.registries;

import com.example.chess.domain.account.VerificationToken;
import com.example.chess.dto.account.UserForm;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class VerificationTokenUserFormRegistry {

    private final Map<VerificationToken, UserForm> userForms = new HashMap<>();

    public synchronized VerificationToken add(UserForm userForm) {
        VerificationToken token = VerificationToken.create();
        userForms.put(token, userForm);
        return token;
    }

    public synchronized Optional<UserForm> get(VerificationToken token) {
        return Optional.ofNullable(userForms.get(token));
    }

    public synchronized boolean containName(String name) {
        return userForms.values().stream()
                .anyMatch(u -> u.getName().equals(name));
    }

    public synchronized boolean containEmail(String email) {
        return userForms.values().stream()
                .anyMatch(u -> u.getEmail().equals(email));
    }

    public synchronized void remove(VerificationToken token) {
        userForms.remove(token);
    }

    public synchronized void clear() {
        userForms.entrySet().removeIf(e -> e.getKey().isExpired());
    }
    
}
