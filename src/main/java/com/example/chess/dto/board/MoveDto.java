package com.example.chess.dto.board;

import com.example.chess.game.board.Move;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Pattern;
import java.util.Optional;

public class MoveDto {

    @Pattern(regexp = "^[a-h][1-8]$", message = "Invalid from value")
    private final String from;
    @Pattern(regexp = "^[a-h][1-8]$", message = "Invalid to value")
    private final String to;
    private final Optional<@Pattern(regexp = "^[pnbrqk]$", message = "Invalid promotion value") String> promotionO;

    @JsonCreator
    public MoveDto(
            @JsonProperty(value = "from", required = true) String from,
            @JsonProperty(value = "to", required = true) String to,
            @JsonProperty("promotion") Optional<String> promotionO
    ) {
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

    public Move toEntity() {
        return new Move(from, to, promotionO);
    }

    public static MoveDto toDto(Move move) {
        return new MoveDto(
                move.getFrom(),
                move.getTo(),
                move.getPromotionO()
        );
    }
}
