package com.example.bankcards.controller;

import com.example.bankcards.abstracts.NotCriticalExceptionAbstract;
import com.example.bankcards.dto.UserEditByAdminDTO;
import com.example.bankcards.dto.UserRegistrationDTO;
import com.example.bankcards.dto.UserSigninDTO;
import com.example.bankcards.facade.UserFacade;
import com.example.bankcards.util.JwtUtil;
import com.example.bankcards.viewModel.BaseResponseViewModel;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/user")
@AllArgsConstructor
public class ApiUserController {

    private final UserFacade userFacade;
    private final JwtUtil jwtUtil;

    @DeleteMapping("/admin/{userId}")
    @Operation(description = "Удаление пользователя администратором")
    public ResponseEntity<BaseResponseViewModel> deleteUserByAdmin(@PathVariable("userId") final String userIdStr){
        final BaseResponseViewModel baseResponseViewModel = new BaseResponseViewModel();
        try {
            this.userFacade.deleteUserByAdmin(userIdStr);
            return ResponseEntity.ok(baseResponseViewModel);
        } catch (Exception e){
            baseResponseViewModel.setError(e.getMessage());
            return ResponseEntity.badRequest().body(baseResponseViewModel);
        }
    }

    @PutMapping("/admin")
    @Operation(description = "Редактирование пользователя администратором")
    public ResponseEntity<BaseResponseViewModel> editUserByAdmin(@RequestBody final UserEditByAdminDTO userEditByAdminDTO){
        final BaseResponseViewModel baseResponseViewModel = new BaseResponseViewModel();
        try {
            this.userFacade.editUserByAdmin(userEditByAdminDTO);
            return ResponseEntity.ok(baseResponseViewModel);
        } catch (Exception e){
            baseResponseViewModel.setError(e.getMessage());
            return ResponseEntity.badRequest().body(baseResponseViewModel);
        }
    }


    @GetMapping("/admin/{userId}")
    @Operation(description = "Получение информации о пользователе администратором")
    public ResponseEntity<BaseResponseViewModel> getInfoForAdmin(@PathVariable("userId") final String userIdStr){
        final BaseResponseViewModel baseResponseViewModel = new BaseResponseViewModel();
        try {
            baseResponseViewModel.setUserInfoForAdminViewModel(this.userFacade.getInfoForAdminByIdStr(userIdStr));
            return ResponseEntity.ok(baseResponseViewModel);
        } catch (NotCriticalExceptionAbstract e) {
            baseResponseViewModel.setError(e.getMessage());
            return ResponseEntity.badRequest().body(baseResponseViewModel);
        } catch (Exception e){
            baseResponseViewModel.setError("unknown");
            return ResponseEntity.badRequest().body(baseResponseViewModel);
        }
    }

    @GetMapping("/profile")
    @Operation(description = "Получение профиля пользователем")
    public ResponseEntity<BaseResponseViewModel> getProfile(){
        final BaseResponseViewModel baseResponseViewModel = new BaseResponseViewModel();
        try {
            baseResponseViewModel.setUserProfileLiteViewModel(this.userFacade.getProfileByUsername(this.jwtUtil.getCurrentAuthUsername()));
            return ResponseEntity.ok(baseResponseViewModel);
        } catch (NotCriticalExceptionAbstract e) {
            baseResponseViewModel.setError(e.getMessage());
            return ResponseEntity.badRequest().body(baseResponseViewModel);
        } catch (Exception e){
            baseResponseViewModel.setError("unknown");
            return ResponseEntity.badRequest().body(baseResponseViewModel);
        }
    }

    @PostMapping("/registration")
    @Operation(description = "Регистрация нового пользователя")
    public ResponseEntity<BaseResponseViewModel> registration(@RequestBody final UserRegistrationDTO userRegistrationDTO){
        final BaseResponseViewModel baseResponseViewModel = new BaseResponseViewModel();
        try {
            baseResponseViewModel.setAccessToken(this.userFacade.registration(userRegistrationDTO));
            return ResponseEntity.ok(baseResponseViewModel);
        } catch (NotCriticalExceptionAbstract e) {
            baseResponseViewModel.setError(e.getMessage());
            return ResponseEntity.badRequest().body(baseResponseViewModel);
        } catch (Exception e){
            baseResponseViewModel.setError("unknown");
            return ResponseEntity.badRequest().body(baseResponseViewModel);
        }
    }

    @PostMapping("/signin")
    @Operation(description = "Аутентификация пользователя")
    public ResponseEntity<BaseResponseViewModel> signin(@RequestBody final UserSigninDTO userSigninDTO){
        final BaseResponseViewModel baseResponseViewModel = new BaseResponseViewModel();
        try {
            baseResponseViewModel.setAccessToken(this.userFacade.signin(userSigninDTO));
            return ResponseEntity.ok(baseResponseViewModel);
        } catch (NotCriticalExceptionAbstract e) {
            baseResponseViewModel.setError(e.getMessage());
            return ResponseEntity.badRequest().body(baseResponseViewModel);
        } catch (Exception e){
            baseResponseViewModel.setError("unknown");
            return ResponseEntity.badRequest().body(baseResponseViewModel);
        }
    }
}
