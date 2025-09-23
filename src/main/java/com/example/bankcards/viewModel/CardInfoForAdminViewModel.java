package com.example.bankcards.viewModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Schema(description = "Данные по карте для администратора")
public class CardInfoForAdminViewModel {
    private Long id;
    private String number;

    @JsonProperty(value = "date_of_expiration")
    private String dateOfExpiration;

    private String status;

    private BigDecimal balance;

    private UserMicroForAdminViewModel userMicroForAdminViewModel;
}
