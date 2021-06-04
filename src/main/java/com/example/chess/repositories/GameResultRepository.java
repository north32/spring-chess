package com.example.chess.repositories;

import com.example.chess.domain.game.GameResult;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameResultRepository extends MongoRepository<GameResult, String> {
}
