package com.example.bankcards.controller;

import com.example.bankcards.abstracts.NotCriticalExceptionAbstract;
import com.example.bankcards.dto.CardTransferNewDTO;
import com.example.bankcards.facade.CardTransferFacade;
import com.example.bankcards.util.JwtUtil;
import com.example.bankcards.viewModel.BaseResponseViewModel;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/card_transfer")
@AllArgsConstructor
public class ApiCardTransferController {

    private final CardTransferFacade cardTransferFacade;
    private final JwtUtil jwtUtil;

    @PutMapping("owns")
    @Operation(description = "Создание платежной операции между собственными картами пользователем")
    public ResponseEntity<BaseResponseViewModel> createBetweenOwns(@RequestBody final CardTransferNewDTO cardTransferNewDTO){
        final BaseResponseViewModel baseResponseViewModel = new BaseResponseViewModel();
        try {
            this.cardTransferFacade.createForOwnCards(
                    this.jwtUtil.getCurrentAuthUsername(),
                    cardTransferNewDTO
            );
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
