package com.example.bankcards.facade;

import com.example.bankcards.abstracts.NotCriticalExceptionAbstract;
import com.example.bankcards.dto.CardTransferNewDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardTransfer;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardTransferFailedException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.model.CardStatus;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.CardTransferRepository;
import com.example.bankcards.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CardTransferFacade {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final CardTransferRepository cardTransferRepository;

    /**
     * Выполнение трасфера денег с одной карты на другую пользователем. Обе карты должны принадлежать владельцу
     * @param cardOwnerUsername
     * @param cardTransferNewDTO
     * @throws NotCriticalExceptionAbstract
     */
    @Transactional(rollbackFor = NotCriticalExceptionAbstract.class)
    public void createForOwnCards(@NotNull final String cardOwnerUsername, @NotNull final CardTransferNewDTO cardTransferNewDTO) throws NotCriticalExceptionAbstract {

        final BigDecimal amount = cardTransferNewDTO.getAmount().setScale(2, RoundingMode.HALF_EVEN);

        final Optional<User> userFrom = this.userRepository.findByUsername(cardOwnerUsername);
        if(userFrom.isEmpty()){
            throw new UserNotFoundException();
        }

        final Optional<Card> cardFrom = this.cardRepository.findById(cardTransferNewDTO.getCardIdFrom());
        if(cardFrom.isEmpty()){
            throw new CardTransferFailedException("карта-источник не найдена");
        }
        if(cardFrom.get().getUser() == null){
            throw new CardTransferFailedException("карта-источник не имеет владельца");
        }
        if(!cardFrom.get().getUser().getId().equals(userFrom.get().getId())){
            throw new CardTransferFailedException("карта-источник не принадлежит пользователю");
        }
        if(cardFrom.get().getStatus() == CardStatus.EXPIRED){
            throw new CardTransferFailedException("карта-источник истек срок обслуживания");
        }
        if(cardFrom.get().getStatus() == CardStatus.BLOCKED){
            throw new CardTransferFailedException("карта-источник заблокирована");
        }
        if(cardFrom.get().getBalance().compareTo(amount) < 0){
            throw new CardTransferFailedException("недостаточно средств");
        }

        final Optional<Card> cardTo = this.cardRepository.findById(cardTransferNewDTO.getCardIdTo());
        if(cardTo.isEmpty()){
            throw new CardTransferFailedException("карта-назначение не найдена");
        }
        if(cardTo.get().getUser() == null){
            throw new CardTransferFailedException("карта-назначение не имеет владельца");
        }
        if(!cardTo.get().getUser().getId().equals(userFrom.get().getId())){
            throw new CardTransferFailedException("карта-назначение не принадлежит пользователю");
        }
        if(cardTo.get().getStatus() == CardStatus.EXPIRED){
            throw new CardTransferFailedException("карта-назначение истек срок обслуживания");
        }
        if(cardTo.get().getStatus() == CardStatus.BLOCKED){
            throw new CardTransferFailedException("карта-назначение заблокирована");
        }

        cardFrom.get().setBalance(cardFrom.get().getBalance().subtract(amount));
        cardTo.get().setBalance(cardFrom.get().getBalance().add(amount));

        final CardTransfer cardTransfer = new CardTransfer();
        cardTransfer.setCardFrom(cardFrom.get());
        cardTransfer.setCardTo(cardTo.get());
        cardTransfer.setAmount(amount);
        cardTransfer.setDateOfAdd(new Date());

        this.cardRepository.save(cardFrom.get());
        this.cardRepository.save(cardTo.get());
        this.cardTransferRepository.save(cardTransfer);
    }
}
