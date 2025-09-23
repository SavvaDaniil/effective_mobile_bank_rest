package com.example.bankcards.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Запрос на регистрацию пользователя")
public class UserRegistrationDTO {
    private final String username;
    private final String password;

    @JsonProperty(value = "is_admin", defaultValue = "false")
    private final boolean isAdmin;
}
