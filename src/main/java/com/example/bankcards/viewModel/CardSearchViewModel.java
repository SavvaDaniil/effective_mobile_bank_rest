package com.example.bankcards.viewModel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardSearchViewModel {
    private Long id;
    private String number;
    private UserMicroForAdminViewModel userMicroForAdminViewModel;
}
