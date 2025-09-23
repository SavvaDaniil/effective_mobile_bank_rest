package com.example.bankcards.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Schema(description = "Запрос на перевод между своими картами")
@NoArgsConstructor
@AllArgsConstructor
public class CardTransferNewDTO {

    @JsonProperty(value = "card_id_from")
    private Long cardIdFrom;

    @JsonProperty(value = "card_id_to")
    private Long cardIdTo;

    private BigDecimal amount;
}
