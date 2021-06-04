package com.example.chess.controllers;

import com.example.chess.domain.Result;
import com.example.chess.dto.session.GameParametersForm;
import com.example.chess.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createGame(
            @AuthenticationPrincipal String id,
            @RequestBody GameParametersForm gameParametersForm
    ) {
        Result<String> result = gameService.createWebGameSession(id, gameParametersForm);
        return result.toResponseEntity();
    }

}
