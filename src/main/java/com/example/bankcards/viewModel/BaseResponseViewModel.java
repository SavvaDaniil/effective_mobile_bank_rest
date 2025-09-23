package com.example.bankcards.viewModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponseViewModel {

    private String error;

    @JsonProperty(value = "access_token")
    private String accessToken;

    private UserProfileLiteViewModel userProfileLiteViewModel;
    private UserInfoForAdminViewModel userInfoForAdminViewModel;

    private CardInfoForAdminViewModel cardInfoForAdminViewModel;
    private List<CardSearchViewModel> cardSearchViewModels;
    private CardInfoForUserViewModel cardInfoForUserViewModel;
}
