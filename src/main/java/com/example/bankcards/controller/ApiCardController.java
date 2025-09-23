package com.example.bankcards.controller;

import com.example.bankcards.abstracts.NotCriticalExceptionAbstract;
import com.example.bankcards.dto.CardEditByAdminDTO;
import com.example.bankcards.dto.CardNewDTO;
import com.example.bankcards.dto.CardSearchDTO;
import com.example.bankcards.facade.CardFacade;
import com.example.bankcards.util.JwtUtil;
import com.example.bankcards.viewModel.BaseResponseViewModel;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/card")
@AllArgsConstructor
public class ApiCardController {

    private final CardFacade cardFacade;
    private final JwtUtil jwtUtil;



    @GetMapping("/{cardId}")
    @Operation(description = "Получение данных карты пользователем по ее id")
    public ResponseEntity<BaseResponseViewModel> getForUserByNumber(@PathVariable("cardId") final String cardIdStr){
        final BaseResponseViewModel baseResponseViewModel = new BaseResponseViewModel();
        try {
            baseResponseViewModel.setCardInfoForUserViewModel(this.cardFacade.getInfoForUserByIdStr(cardIdStr));
            return ResponseEntity.ok(baseResponseViewModel);
        } catch (NotCriticalExceptionAbstract e) {
            baseResponseViewModel.setError(e.getMessage());
            return ResponseEntity.badRequest().body(baseResponseViewModel);
        } catch (Exception e){
            baseResponseViewModel.setError("unknown");
            return ResponseEntity.badRequest().body(baseResponseViewModel);
        }
    }

    @PostMapping("/search")
    @Operation(description = "Поиск карт пользователем")
    public ResponseEntity<BaseResponseViewModel> searchByUser(@RequestBody final CardSearchDTO cardSearchDTO){
        final BaseResponseViewModel baseResponseViewModel = new BaseResponseViewModel();
        try {
            baseResponseViewModel.setCardSearchViewModels(this.cardFacade.searchByUser(this.jwtUtil.getCurrentAuthUsername(), cardSearchDTO));
            return ResponseEntity.ok(baseResponseViewModel);
        } catch (NotCriticalExceptionAbstract e) {
            baseResponseViewModel.setError(e.getMessage());
            return ResponseEntity.badRequest().body(baseResponseViewModel);
        } catch (Exception e){
            baseResponseViewModel.setError("unknown");
            return ResponseEntity.badRequest().body(baseResponseViewModel);
        }
    }

    @PostMapping("/admin/search")
    @Operation(description = "Поиск карт администратором")
    public ResponseEntity<BaseResponseViewModel> searchByAdmin(@RequestBody final CardSearchDTO cardSearchDTO){
        final BaseResponseViewModel baseResponseViewModel = new BaseResponseViewModel();
        try {
            baseResponseViewModel.setCardSearchViewModels(this.cardFacade.searchByAdmin(cardSearchDTO));
            return ResponseEntity.ok(baseResponseViewModel);
        } catch (Exception e){
            baseResponseViewModel.setError("unknown");
            return ResponseEntity.badRequest().body(baseResponseViewModel);
        }
    }

    @DeleteMapping("/admin/{cardId}")
    @Operation(description = "Удаление карты администратором")
    public ResponseEntity<BaseResponseViewModel> deleteByAdmin(@PathVariable("cardId") final String cardIdStr){
        final BaseResponseViewModel baseResponseViewModel = new BaseResponseViewModel();
        try {
            this.cardFacade.deleteByIdStr(cardIdStr);
            return ResponseEntity.ok(baseResponseViewModel);
        } catch (NotCriticalExceptionAbstract e) {
            baseResponseViewModel.setError(e.getMessage());
            return ResponseEntity.badRequest().body(baseResponseViewModel);
        } catch (Exception e){
            baseResponseViewModel.setError("unknown");
            return ResponseEntity.badRequest().body(baseResponseViewModel);
        }
    }

    @PutMapping("/admin/{cardId}")
    @Operation(description = "Редактирование карты администратором")
    public ResponseEntity<BaseResponseViewModel> editByAdmin(
            @PathVariable("cardId") final String cardIdStr,
            @RequestBody final CardEditByAdminDTO cardEditByAdminDTO){
        final BaseResponseViewModel baseResponseViewModel = new BaseResponseViewModel();
        try {
            this.cardFacade.editByAdmin(cardIdStr, cardEditByAdminDTO);
            return ResponseEntity.ok(baseResponseViewModel);
        } catch (NotCriticalExceptionAbstract e) {
            baseResponseViewModel.setError(e.getMessage());
            return ResponseEntity.badRequest().body(baseResponseViewModel);
        } catch (Exception e){
            baseResponseViewModel.setError("unknown");
            return ResponseEntity.badRequest().body(baseResponseViewModel);
        }
    }

    @GetMapping("/admin/{cardId}")
    @Operation(description = "Получение информации по карте по id карты")
    public ResponseEntity<BaseResponseViewModel> getInfoById(@PathVariable("cardId") final String cardId){
        final BaseResponseViewModel baseResponseViewModel = new BaseResponseViewModel();
        try {
            baseResponseViewModel.setCardInfoForAdminViewModel(this.cardFacade.getInfoById(cardId));
            return ResponseEntity.ok(baseResponseViewModel);
        } catch (NotCriticalExceptionAbstract e) {
            baseResponseViewModel.setError(e.getMessage());
            return ResponseEntity.badRequest().body(baseResponseViewModel);
        } catch (Exception e){
            baseResponseViewModel.setError("unknown");
            return ResponseEntity.badRequest().body(baseResponseViewModel);
        }
    }

//    @GetMapping("/admin/number/{cardNumber}")
//    @Operation(description = "Получение информации по карте по номеру карты")
//    public ResponseEntity<BaseResponseViewModel> getInfoByNumber(@PathVariable("cardNumber") String cardNumber){
//        final BaseResponseViewModel baseResponseViewModel = new BaseResponseViewModel();
//        try {
//            baseResponseViewModel.setCardInfoForAdminViewModel(this.cardFacade.getInfoByCardNumber(cardNumber));
//            return ResponseEntity.ok(baseResponseViewModel);
//        } catch (NotCriticalExceptionAbstract e) {
//            baseResponseViewModel.setError(e.getMessage());
//            return ResponseEntity.badRequest().body(baseResponseViewModel);
//        } catch (Exception e){
//            baseResponseViewModel.setError("unknown");
//            return ResponseEntity.badRequest().body(baseResponseViewModel);
//        }
//    }

    @PutMapping("/admin")
    @Operation(description = "Добавление карты для пользователя")
    public ResponseEntity<BaseResponseViewModel> createForUser(@RequestBody final CardNewDTO cardNewDTO){
        final BaseResponseViewModel baseResponseViewModel = new BaseResponseViewModel();
        try {
            baseResponseViewModel.setCardInfoForAdminViewModel(this.cardFacade.createForUser(cardNewDTO));
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
