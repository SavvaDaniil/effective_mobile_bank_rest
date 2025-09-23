package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "Запрос на блокировку карты от пользователя")
public class CardBlockRequestNewDTO {
    private final String cardId;
}
