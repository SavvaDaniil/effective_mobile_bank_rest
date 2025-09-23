package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на редактирование профиля пользователем")
public class UserEditByUserDTO {
    private final String username;
}
