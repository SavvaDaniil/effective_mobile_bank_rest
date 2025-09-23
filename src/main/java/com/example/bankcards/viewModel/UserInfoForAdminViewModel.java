package com.example.bankcards.viewModel;

import com.example.bankcards.model.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoForAdminViewModel {
    private Long id;
    private String username;
    private Role role;

    @JsonProperty(value = "date_of_add")
    private String dateOfAdd;

    @JsonProperty(value = "date_of_update")
    private String dateOfUpdate;
}
