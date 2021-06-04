package com.example.chess.service;

import com.example.chess.domain.account.VerificationToken;

import javax.mail.MessagingException;

public interface MailService {

    void sendTokenVerificationMail(String address, String name, VerificationToken token) throws MessagingException;

}
