package com.example.bankcards.facade;

import com.example.bankcards.abstracts.NotCriticalExceptionAbstract;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardBlockRequestCreateFailedException;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.model.CardStatus;
import com.example.bankcards.repository.CardBlockRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CardBlockRequestFacade {

    private final CardBlockRequestRepository cardBlockRequestRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    public void createByUser(@NotNull final String userUsername, @NotNull final String cardIdStr) throws NotCriticalExceptionAbstract {
        Long cardId = 0L;
        try {
            cardId = Long.parseLong(cardIdStr);
        } catch (NumberFormatException e){
            throw new CardNotFoundException();
        }

        final Optional<User> user = this.userRepository.findByUsername(userUsername);
        if(user.isEmpty()){
            throw new UserNotFoundException();
        }

        final Optional<Card> card = this.cardRepository.findById(cardId);
        if(card.isEmpty()){
            throw new CardNotFoundException();
        }
        if(card.get().getStatus() == CardStatus.EXPIRED){
            throw new CardBlockRequestCreateFailedException("срок годности карты истёк, карта не действительна");
        }
        if(card.get().getStatus() == CardStatus.BLOCKED){
            throw new CardBlockRequestCreateFailedException("карта уже заблокирована");
        }
        if(!card.get().getUser().getId().equals(user.get().getId())){
            throw new CardBlockRequestCreateFailedException("пользователь не является владельцем карты");
        }

        final Optional<CardBlockRequest> cardBlockRequestAlreadyExists = this.cardBlockRequestRepository.findByUserIdAndCardId(user.get().getId(), card.get().getId());
        if(cardBlockRequestAlreadyExists.isPresent()){
            if(cardBlockRequestAlreadyExists.get().getCard() != null && cardBlockRequestAlreadyExists.get().getCard().getStatus() == CardStatus.BLOCKED){
                throw new CardBlockRequestCreateFailedException("запрос на блокировку карты уже был отправлен и выполнен");
            }
            throw new CardBlockRequestCreateFailedException("запрос на блокировку карты уже был отправлен");
        }

        final CardBlockRequest cardBlockRequest = new CardBlockRequest();
        cardBlockRequest.setCard(card.get());
        cardBlockRequest.setUser(user.get());
        cardBlockRequest.setDateOfAdd(new Date());
        this.cardBlockRequestRepository.save(cardBlockRequest);
    }
}
