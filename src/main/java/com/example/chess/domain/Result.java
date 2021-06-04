package com.example.chess.domain;

import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

public class Result<T> {

    private final Optional<T> valueO;
    private final Set<String> errors;

    private Result(Optional<T> valueO) {
        this.valueO = valueO;
        this.errors = new HashSet<>();
    }

    public void addError(String error) {
        errors.add(error);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public T getValue() {
        return valueO.orElseThrow();
    }

    public Set<String> getErrors() {
        if (errors.isEmpty()) {
            throw new NoSuchElementException("Non error result doesn't contain message.");
        }
        return errors;
    }

    public ResponseEntity<?> toResponseEntity() {
        if (hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        if (valueO.isPresent()) {
            return ResponseEntity.ok(valueO.get());
        }
        return ResponseEntity.ok().build();
    }

    public static <T> Result<T> of(T value) {
        return new Result<>(Optional.ofNullable(value));
    }

    public static <T> Result<T> ofOptional(Optional<T> value) {
        return new Result<>(value);
    }

    public static <T> Result<T> empty() {
        return new Result<>(Optional.empty());
    }

    public static <T> Result<T> error(String error) {
        Result<T> result = empty();
        result.addError(error);
        return result;
    }

}
