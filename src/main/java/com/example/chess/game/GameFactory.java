package com.example.chess.game;

import com.example.chess.game.player.Player;
import com.example.chess.service.GameResultService;

public interface GameFactory {

    Game createGame(GameResultService gameResultService, Mode mode, Player player1, Player player2);

    Game createGameVsBot(GameResultService gameResultService, Mode mode, Player player);

}
