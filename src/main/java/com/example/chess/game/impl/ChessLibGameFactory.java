package com.example.chess.game.impl;

import com.example.chess.game.Game;
import com.example.chess.game.GameFactory;
import com.example.chess.game.Mode;
import com.example.chess.game.Side;
import com.example.chess.game.board.Board;
import com.example.chess.game.impl.board.ChessLibBoard;
import com.example.chess.game.impl.player.ChessLibBot;
import com.example.chess.game.player.Player;
import com.example.chess.service.GameResultService;
import com.github.bhlangonijr.kengine.SearchParams;
import com.github.bhlangonijr.kengine.SearchState;
import com.github.bhlangonijr.kengine.alphabeta.AlphaBetaSearch;
import org.springframework.stereotype.Component;

@Component
public class ChessLibGameFactory implements GameFactory {

    private static final SearchParams SEARCH_PARAMS = new SearchParams(
            60000000,
            60000000,
            0,
            0,
            60000,
            3, // 100
            1,
            50000000,
            "",
            false,
            false,
            1
    );

    @Override
    public Game createGame(GameResultService gameResultService, Mode mode, Player player1, Player player2) {
        Board board = new ChessLibBoard();
        Game game = new Game(gameResultService, player1, player2, mode, board);
        player1.setGame(game);
        player2.setGame(game);
        return game;
    }

    @Override
    public Game createGameVsBot(GameResultService gameResultService, Mode mode, Player player) {
        ChessLibBoard board = new ChessLibBoard();
        Player bot = createBot(board, player.getSide().opposite());
        Game game = new Game(gameResultService, player, bot, mode, board);
        player.setGame(game);
        bot.setGame(game);
        return game;
    }

    private Player createBot(ChessLibBoard board, Side side) {
        AlphaBetaSearch search = new AlphaBetaSearch();
        SearchState state = new SearchState(SEARCH_PARAMS, board.getChessLibBoard());
        return new ChessLibBot(side, search, state);
    }

}
