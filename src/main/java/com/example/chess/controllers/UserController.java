package com.example.chess.controllers;

import com.example.chess.domain.Result;
import com.example.chess.domain.account.User;
import com.example.chess.domain.account.VerificationToken;
import com.example.chess.dto.account.EmailForm;
import com.example.chess.dto.account.UserForm;
import com.example.chess.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserForm userForm) {
        Result<Void> result = userService.register(userForm);
        return result.toResponseEntity();
    }

    @PostMapping("/verify/{token}")
    public ResponseEntity<?> verify(@PathVariable VerificationToken token) {
        Result<User> result = userService.verify(token);
        return result.toResponseEntity();
    }

    @PostMapping("/verify/email")
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody EmailForm emailForm) {
        Result<Void> result = userService.verifyEmail(emailForm);
        return result.toResponseEntity();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getAccount(@AuthenticationPrincipal String id) {
        Result<User> result = userService.getUserById(id);
        return result.toResponseEntity();
    }

}
