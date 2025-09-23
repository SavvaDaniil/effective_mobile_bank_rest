package com.example.bankcards.viewModel;

import com.example.bankcards.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserMicroForAdminViewModel {
    private Long id;
    private String username;
    private Role role;
}
