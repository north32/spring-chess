package com.example.chess.game.board;

import java.util.Optional;

public class Move {

    private final String from;
    private final String to;
    private final Optional<String> promotionO;

    public Move(String from, String to, Optional<String> promotionO) {
        this.from = from;
        this.to = to;
        this.promotionO = promotionO;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Optional<String> getPromotionO() {
        return promotionO;
    }
}
