package com.example.chess.service.impl;

import com.example.chess.domain.Result;
import com.example.chess.domain.account.User;
import com.example.chess.domain.account.VerificationToken;
import com.example.chess.dto.account.EmailForm;
import com.example.chess.dto.account.UserForm;
import com.example.chess.registries.VerificationTokenUserFormRegistry;
import com.example.chess.registries.VerificationTokenUserRegistry;
import com.example.chess.repositories.UserRepository;
import com.example.chess.service.MailService;
import com.example.chess.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final VerificationTokenUserFormRegistry userFormRegistry;
    private final VerificationTokenUserRegistry userRegistry;
    private final MailService mailService;

    public UserServiceImpl(
            UserRepository userRepository,
            VerificationTokenUserFormRegistry userFormRegistry,
            VerificationTokenUserRegistry userRegistry,
            MailService mailService
    ) {
        this.userRepository = userRepository;
        this.userFormRegistry = userFormRegistry;
        this.userRegistry = userRegistry;
        this.mailService = mailService;
    }

    @Override
    public Result<Void> register(UserForm userForm) {
        Result<Void> result = Result.empty();
        if (
            userRepository.existsByName(userForm.getName()) ||
            userFormRegistry.containName(userForm.getName())
        ) {
            result.addError("Username has already been taken");
        }
        if (
            userRepository.existsByEmail(userForm.getEmail()) ||
            userFormRegistry.containEmail(userForm.getEmail())
        ) {
            result.addError("Email already exists");
        }
        if (result.hasErrors()) {
            return result;
        }
        return sendVerificationToken(userForm);
    }

    @Override
    public Result<Void> verifyEmail(EmailForm emailForm) {
        String email = emailForm.getEmail();
        Optional<User> userO = userRepository.findByEmail(email);
        if (userO.isEmpty()) {
            return Result.error("Email doesn't exist");
        }
        return sendVerificationToken(userO.get());
    }


    @Override
    public Result<User> verify(VerificationToken token) {
        Optional<User> userO = userRegistry.get(token);
        Optional<UserForm> userFormO = userFormRegistry.get(token);
        if (userO.isEmpty() && userFormO.isEmpty()) {
            return Result.error("Token doesn't exist");
        }
        if (userO.isPresent()) {
            User user = userO.get();
            userRegistry.remove(token);
            authenticate(user);
            return Result.of(user);
        }
        UserForm userForm = userFormO.get();
        User user = userForm.toEntity();
        user = userRepository.save(user);
        userFormRegistry.remove(token);
        authenticate(user);
        return Result.of(user);
    }

    @Override
    public Result<User> getUserById(String id) {
        Optional<User> user = userRepository.findById(id);
        return Result.ofOptional(user);
    }

    private Result<Void> sendVerificationToken(User user) {
        VerificationToken token = userRegistry.add(user);
        try {
            mailService.sendTokenVerificationMail(user.getEmail(), user.getName(), token);
        } catch (Exception e) {
            userRegistry.remove(token);
            return Result.error("Mail sending error");

        }
        return Result.empty();
    }

    private Result<Void> sendVerificationToken(UserForm userForm) {
        VerificationToken token = userFormRegistry.add(userForm);
        try {
            mailService.sendTokenVerificationMail(userForm.getEmail(), userForm.getName(), token);
        } catch (Exception e) {
            userFormRegistry.remove(token);
            return Result.error("Mail sending error");

        }
        return Result.empty();
    }

    private void authenticate(User user) {
        String principal = user.getId();
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList("USER");
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, authorityList);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
