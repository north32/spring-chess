package com.example.chess.service.impl;

import com.example.chess.domain.game.GameResult;
import com.example.chess.repositories.GameResultRepository;
import com.example.chess.service.GameResultService;
import org.springframework.stereotype.Service;

@Service
public class GameResultServiceImpl implements GameResultService {

    private final GameResultRepository gameResultRepository;

    public GameResultServiceImpl(GameResultRepository gameResultRepository) {
        this.gameResultRepository = gameResultRepository;
    }

    @Override
    public void saveGameResult(GameResult result) {
        if (result.getWhitePlayerUserIdO().isPresent() || result.getBlackPlayerUserIdO().isPresent()) {
            gameResultRepository.save(result);
        }
    }
}
