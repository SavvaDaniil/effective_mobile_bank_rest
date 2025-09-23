package com.example.bankcards.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на поиск карт")
public class CardSearchDTO {
    private final int page;

    @JsonProperty(value = "search_query")
    private final String searchQuery;
}
