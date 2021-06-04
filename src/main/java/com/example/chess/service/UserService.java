package com.example.chess.service;

import com.example.chess.domain.Result;
import com.example.chess.domain.account.User;
import com.example.chess.domain.account.VerificationToken;
import com.example.chess.dto.account.EmailForm;
import com.example.chess.dto.account.UserForm;

public interface UserService {

    Result<Void> register(UserForm userForm);

    Result<Void> verifyEmail(EmailForm emailForm);

    Result<User> verify(VerificationToken token);

    Result<User> getUserById(String id);

}
