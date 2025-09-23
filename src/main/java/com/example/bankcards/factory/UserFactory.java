package com.example.bankcards.factory;

import com.example.bankcards.entity.User;
import com.example.bankcards.viewModel.UserInfoForAdminViewModel;
import com.example.bankcards.viewModel.UserMicroForAdminViewModel;
import com.example.bankcards.viewModel.UserProfileLiteViewModel;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class UserFactory {

    private final SimpleDateFormat simpleDateFormatDateOfUpdate = new SimpleDateFormat("yyyy-MM-dd");

    @NotNull
    public UserInfoForAdminViewModel createInfoForAdmin(@NotNull final User user){
        return new UserInfoForAdminViewModel(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.getDateOfAdd() != null ? this.simpleDateFormatDateOfUpdate.format(user.getDateOfAdd()) : null,
                user.getDateOfUpdate() != null ? this.simpleDateFormatDateOfUpdate.format(user.getDateOfUpdate()) : null
        );
    }

    @NotNull
    public UserMicroForAdminViewModel createMicro(@NotNull final User user){
        return new UserMicroForAdminViewModel(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }

    @NotNull
    public UserProfileLiteViewModel createProfile(@NotNull final User user){
        return new UserProfileLiteViewModel(user.getUsername());
    }
}
