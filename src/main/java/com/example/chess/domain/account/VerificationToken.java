package com.example.chess.domain.account;


import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class VerificationToken {

    public static final long VERIFICATION_TIMEOUT_M = 60;

    private final String token;
    private final Optional<Instant> expiresO;

    public VerificationToken(String token) {
        this.token = token;
        this.expiresO = Optional.empty();
    }

    public VerificationToken (String token, Duration timeout) {
        this.token = token;
        this.expiresO = Optional.of(Instant.now().plus(timeout));
    }

    public static VerificationToken create() {
        UUID id = UUID.randomUUID();
        return new VerificationToken(id.toString(), Duration.ofMinutes(VERIFICATION_TIMEOUT_M));
    }

    public boolean isExpired() {
        if (expiresO.isEmpty()) {
            return false;
        }
        Instant expires = expiresO.get();
        return expires.isAfter(Instant.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VerificationToken that = (VerificationToken) o;
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }

    @Override
    public String toString() {
        return token;
    }
}
