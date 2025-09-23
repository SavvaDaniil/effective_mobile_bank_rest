package com.example.bankcards.factory;

import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.CardInfoWrongException;
import com.example.bankcards.viewModel.CardInfoForAdminViewModel;
import com.example.bankcards.viewModel.CardInfoForUserViewModel;
import com.example.bankcards.viewModel.CardSearchViewModel;
import com.example.bankcards.viewModel.UserMicroForAdminViewModel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
@AllArgsConstructor
public class CardFactory {

    private final UserFactory userFactory;
    private static final SimpleDateFormat CARD_DATE_OF_EXPIRATION_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String CARD_NUMBER_MASK_FOR_USER = "**** **** **** ";

    @NotNull
    public CardSearchViewModel createSearch(@NotNull final Card card){
        UserMicroForAdminViewModel userMicroForAdminViewModel = null;
        if(card.getUser() != null){
            userMicroForAdminViewModel = this.userFactory.createMicro(card.getUser());
        }
        return new CardSearchViewModel(
                card.getId(),
                card.getNumber(),
                userMicroForAdminViewModel
        );
    }

    @NotNull
    public CardInfoForUserViewModel createInfoForUser(@NotNull final Card card){
        String cardStatus = "";
        switch (card.getStatus()){
            case ACTIVE -> cardStatus = "Активна";
            case BLOCKED -> cardStatus = "Заблокирована";
            case EXPIRED -> cardStatus = "Истек срок";
            default -> cardStatus = "Ошибка статуса";
        }

        return new CardInfoForUserViewModel(
                CARD_NUMBER_MASK_FOR_USER + card.getNumber().substring(12),
                card.getDateOfExpiration() != null ? CARD_DATE_OF_EXPIRATION_DATE_FORMAT.format(card.getDateOfExpiration()) : null,
                cardStatus,
                card.getBalance()
        );
    }

    @NotNull
    public CardInfoForAdminViewModel createInfoForAdmin(@NotNull final Card card) throws CardInfoWrongException {
        String cardStatus = "";
        switch (card.getStatus()){
            case ACTIVE -> cardStatus = "Активна";
            case BLOCKED -> cardStatus = "Заблокирована";
            case EXPIRED -> cardStatus = "Истек срок";
            default -> cardStatus = "ошибка статуса";
        }

        UserMicroForAdminViewModel userMicroForAdminViewModel = null;
        if(card.getUser() != null){
            userMicroForAdminViewModel = this.userFactory.createMicro(card.getUser());
        }

        if(card.getNumber().length() < 16){
            throw new CardInfoWrongException("Длина номера карты меньше 16 символов");
        }

        return new CardInfoForAdminViewModel(
                card.getId(),
                card.getNumber(),
                card.getDateOfExpiration() != null ? CARD_DATE_OF_EXPIRATION_DATE_FORMAT.format(card.getDateOfExpiration()) : null,
                cardStatus,
                card.getBalance(),
                userMicroForAdminViewModel
        );
    }
}
