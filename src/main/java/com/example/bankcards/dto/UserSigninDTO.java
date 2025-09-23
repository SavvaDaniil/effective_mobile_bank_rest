package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на аутентификацию пользователя")
public class UserSigninDTO {
    private final String username;
    private final String password;
}
