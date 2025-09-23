package com.example.bankcards.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на добавление новой карты для пользователя")
public class CardNewDTO {
    @JsonProperty(value = "user_username")
    private String userUsername;

    @JsonProperty(value = "card_number")
    private String cardNumber;

    @JsonProperty(value = "card_date_of_expiration")
    private String cardDateOfExpiration;
}
