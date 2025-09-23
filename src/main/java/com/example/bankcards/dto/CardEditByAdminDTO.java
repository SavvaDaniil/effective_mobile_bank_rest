package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Запрос на редактирование карты администратором")
@NoArgsConstructor
public class CardEditByAdminDTO {
    private String status;
}
