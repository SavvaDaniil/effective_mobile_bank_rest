package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на редактирование профиля администратором")
public class UserEditByAdminDTO {
    private final Long id;
    private final String username;
}
