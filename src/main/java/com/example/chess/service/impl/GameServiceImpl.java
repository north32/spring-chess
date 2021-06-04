package com.example.chess.service.impl;

import com.example.chess.domain.Result;
import com.example.chess.dto.session.GameParametersForm;
import com.example.chess.game.GameFactory;
import com.example.chess.game.Mode;
import com.example.chess.game.Side;
import com.example.chess.game.exceptions.GameSessionException;
import com.example.chess.game.player.Player;
import com.example.chess.game.player.UnknownPlayer;
import com.example.chess.game.player.UserPlayer;
import com.example.chess.game.registries.GameSearchRegistry;
import com.example.chess.game.registries.GameSessionRegistry;
import com.example.chess.game.session.GameParameters;
import com.example.chess.game.session.GameSession;
import com.example.chess.game.session.web.WebGameSession;
import com.example.chess.service.GameResultService;
import com.example.chess.service.GameService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    private final ApplicationContext applicationContext;
    private final GameSessionRegistry gameSessionRegistry;
    private final GameSearchRegistry gameSearchRegistry;
    private final GameFactory gameFactory;
    private final GameResultService gameResultService;

    public GameServiceImpl(
            ApplicationContext applicationContext,
            GameSessionRegistry gameSessionRegistry,
            GameSearchRegistry gameSearchRegistry,
            GameFactory gameFactory,
            GameResultService gameResultService
    ) {
        this.applicationContext = applicationContext;
        this.gameSessionRegistry = gameSessionRegistry;
        this.gameSearchRegistry = gameSearchRegistry;
        this.gameFactory = gameFactory;
        this.gameResultService = gameResultService;
    }

    @Override
    public Result<String> createWebGameSession(String id, GameParametersForm parametersForm) {
        Mode mode = parametersForm.getMode();
        GameParameters.Builder builder = GameParameters.builder(mode)
                .userId(id);
        Optional<String> opponentSessionIdO = parametersForm.getOpponentSessionIdO();
        if (mode == Mode.PLAYER_VS_PLAYER) {
            if (opponentSessionIdO.isPresent()) {
                String opponentSessionId = opponentSessionIdO.get();
                builder.opponentSessionId(opponentSessionId);
                Optional<GameSession> opponentGameSessionO = gameSessionRegistry.getById(opponentSessionId);
                if (opponentGameSessionO.isPresent()) {
                    GameSession opponentGameSession = opponentGameSessionO.get();
                    GameParameters opponentGameParameters = opponentGameSession.getParameters();
                    Side opponentSide = opponentGameParameters.getSide();
                    builder.side(opponentSide.opposite());
                } else {
                    return Result.error("No such opponent session found");
                }
            }
        } else {
            if (opponentSessionIdO.isPresent()) {
                return Result.error("Can't create game in this mode with opponent");
            }
            Optional<Side> sideO = parametersForm.getSideO();
            builder.side(sideO.orElse(Side.WHITE));
        }
        GameParameters parameters = builder.build();
        GameSession session = new WebGameSession(applicationContext, parameters);
        gameSessionRegistry.add(session);
        return Result.of(session.getId());
    }

    @Override
    public void sessionConnected(GameSession session) {
        if (session.isInGame()) {
            throw new GameSessionException("Session already in game");
        }
        GameParameters parameters = session.getParameters();
        Mode mode = parameters.getMode();
        if (mode == Mode.PLAYER_VS_MACHINE) {
            Player player = createPlayer(parameters, session);
            gameFactory.createGameVsBot(gameResultService, parameters.getMode(), player);

        } else if (mode == Mode.PLAYER_VS_PLAYER) {
            Optional<String> opponentSessionIdO = parameters.getOpponentSessionIdO();
            if (opponentSessionIdO.isPresent()) {
                String opponentId = opponentSessionIdO.get();
                Optional<GameSession> opponentSessionO = gameSessionRegistry.getById(opponentId);
                if (opponentSessionO.isPresent()) {
                    GameSession opponentSession = opponentSessionO.get();
                    if (opponentSession.isInGame()) {
                        throw new GameSessionException("Opposite session already in game");
                    }
                    Player player1 = createPlayer(parameters, session);
                    Player player2 = createPlayer(opponentSession.getParameters(), opponentSession);
                    gameFactory.createGame(gameResultService, mode, player1, player2);
                } else {
                    throw new GameSessionException("Opposite session not connected");
                }
            }
        } else if (mode == Mode.PLAYER_VS_RANDOM) {
            Optional<GameSession> opponentSessionO = gameSearchRegistry.getSession();
            if (opponentSessionO.isPresent()) {
                GameSession opponentSession = opponentSessionO.get();
                if (opponentSession.isInGame()) {
                    throw new GameSessionException("Opposite session already in game");
                }
                GameParameters opponentParameters = opponentSession.getParameters();
                if (isSameUser(parameters, opponentParameters)) {
                    gameSearchRegistry.add(session);
                    gameSearchRegistry.add(opponentSession);
                } else {
                    parameters = GameParameters.updateSide(parameters, Side.WHITE);
                    opponentParameters = GameParameters.updateSide(opponentParameters, Side.BLACK);
                    Player player1 = createPlayer(parameters, session);
                    Player player2 = createPlayer(opponentParameters, opponentSession);
                    gameFactory.createGame(gameResultService, mode, player1, player2);
                }
            } else {
                gameSearchRegistry.add(session);
            }

        }
    }

    private Player createPlayer(GameParameters parameters, GameSession session) {
        Optional<String> userIdO = parameters.getUserIdO();
        Side side = parameters.getSide();
        if (userIdO.isPresent()) {
            return new UserPlayer(side, session, userIdO.get());
        }
        return new UnknownPlayer(side, session);
    }

    private boolean isSameUser(GameParameters parameters1, GameParameters parameters2) {
        Optional<String> userIdO1 = parameters1.getUserIdO();
        Optional<String> userIdO2 = parameters2.getUserIdO();
        if (userIdO1.isPresent() && userIdO2.isPresent()) {
            return userIdO1.get().equals(userIdO2.get());
        }
        return false;
    }

    @Override
    public void sessionDisconnected(GameSession session) {
        session.close();
        gameSearchRegistry.remove(session);
        gameSessionRegistry.remove(session);
    }

}
