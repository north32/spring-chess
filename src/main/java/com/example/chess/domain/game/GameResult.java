package com.example.chess.domain.game;

import com.example.chess.game.Mode;
import com.example.chess.game.State;
import com.example.chess.game.board.Move;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Document(collection = "users")
public class GameResult {

    @Id
    private String id;
    private Mode mode;
    private State state;
    private Optional<String> whitePlayerUserIdO;
    private Optional<String> blackPlayerUserIdO;
    @DBRef(lazy = true)
    private List<Move> moveList;
    @CreatedDate
    private Date date;

    public GameResult(
            Mode mode,
            State state,
            Optional<String> whitePlayerUserIdO,
            Optional<String> blackPlayerUserIdO,
            List<Move> moveList
    ) {
        this.mode = mode;
        this.state = state;
        this.whitePlayerUserIdO = whitePlayerUserIdO;
        this.blackPlayerUserIdO = blackPlayerUserIdO;
        this.moveList = moveList;
    }

    public String getId() {
        return id;
    }

    public Mode getMode() {
        return mode;
    }

    public State getState() {
        return state;
    }

    public Optional<String> getWhitePlayerUserIdO() {
        return whitePlayerUserIdO;
    }

    public Optional<String> getBlackPlayerUserIdO() {
        return blackPlayerUserIdO;
    }

    public List<Move> getMoveList() {
        return moveList;
    }

    public Date getDate() {
        return date;
    }

    public static class Builder {
        private final Mode mode;
        private final State state;
        private Optional<String> whitePlayerUserIdO;
        private Optional<String> blackPlayerUserIdO;
        private List<Move> moveList = List.of();

        public Builder(Mode mode, State state) {
            this.mode = mode;
            this.state = state;
        }

        public Builder whitePlayerUserId(String whitePlayerUserId) {
            this.whitePlayerUserIdO = Optional.of(whitePlayerUserId);
            return this;
        }

        public Builder blackPlayerUserId(String blackPlayerUserId) {
            this.blackPlayerUserIdO = Optional.of(blackPlayerUserId);
            return this;
        }

        public Builder moveList(List<Move> moveList) {
            this.moveList = moveList;
            return this;
        }

        public GameResult build() {
            return new GameResult(mode, state, whitePlayerUserIdO, blackPlayerUserIdO, moveList);
        }
    }
}
