package com.example.bankcards.controller;

import com.example.bankcards.abstracts.NotCriticalExceptionAbstract;
import com.example.bankcards.facade.CardBlockRequestFacade;
import com.example.bankcards.util.JwtUtil;
import com.example.bankcards.viewModel.BaseResponseViewModel;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/card_block_request")
@AllArgsConstructor
public class ApiCardBlockRequestController {

    private final CardBlockRequestFacade cardBlockRequestFacade;
    private final JwtUtil jwtUtil;

    @PutMapping("/{cardId}")
    @Operation(description = "Запрос на блокировку карты пользователем")
    public ResponseEntity<BaseResponseViewModel> create(@PathVariable("cardId") final String cardIdStr){
        final BaseResponseViewModel baseResponseViewModel = new BaseResponseViewModel();
        try {
            this.cardBlockRequestFacade.createByUser(this.jwtUtil.getCurrentAuthUsername(), cardIdStr);
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
