package com.example.chess.service.impl;

import com.example.chess.domain.account.VerificationToken;
import com.example.chess.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
@PropertySource("classpath:application.properties")
public class MailServiceImpl implements MailService {

    @Value("${mail.baseUrl}")
    private String baseUrl;
    @Value("${spring.mail.username}")
    private String from;
    @Value("${mail.subject}")
    private String subject;


    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public MailServiceImpl(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendTokenVerificationMail(String address, String name, VerificationToken token) throws MessagingException {
        String verificationLink = "http://" + baseUrl + "/user/verify/" + token;
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("verificationLink", verificationLink);
        String text = templateEngine.process("mail", context);


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
        helper.setFrom(from);
        helper.setTo(address);
        helper.setSubject(subject);
        helper.setText(text, true);

        System.out.println(verificationLink);
        // mailSender.send(message);
    }

}
