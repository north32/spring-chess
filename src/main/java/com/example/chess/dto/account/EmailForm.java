package com.example.chess.dto.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;

public class EmailForm {

    @Email(message = "Invalid mail value")
    private String email;

    @JsonCreator
    public EmailForm(
            @JsonProperty(value = "email", required = true) String email
    ) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
