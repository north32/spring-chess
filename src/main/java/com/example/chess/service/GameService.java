package com.example.chess.service;

import com.example.chess.domain.Result;
import com.example.chess.dto.session.GameParametersForm;
import com.example.chess.game.session.GameSession;

public interface GameService {

    Result<String> createWebGameSession(String id, GameParametersForm parametersForm);

    void sessionConnected(GameSession session);

    void sessionDisconnected(GameSession session);
}
