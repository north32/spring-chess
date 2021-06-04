package com.example.chess.dto.account;

import com.example.chess.domain.account.User;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

public class UserForm {

    @Email(message = "Invalid mail value")
    private String email;
    @Pattern(regexp = "[a-zA-Z0-9]{1,30}$", message = "Invalid name value")
    private String name;

    @JsonCreator
    public UserForm(
            @JsonProperty(value = "email", required = true) String email,
            @JsonProperty(value = "name", required = true) String name
    ) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public User toEntity() {
        return new User(email, name);
    }
}
