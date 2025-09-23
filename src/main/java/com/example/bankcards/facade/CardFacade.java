package com.example.bankcards.facade;

import com.example.bankcards.abstracts.NotCriticalExceptionAbstract;
import com.example.bankcards.dto.CardEditByAdminDTO;
import com.example.bankcards.dto.CardNewDTO;
import com.example.bankcards.dto.CardSearchDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardBlockRequest;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardCreateFailedException;
import com.example.bankcards.exception.CardEditFailedException;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.factory.CardFactory;
import com.example.bankcards.model.CardStatus;
import com.example.bankcards.repository.CardBlockRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.viewModel.CardInfoForAdminViewModel;
import com.example.bankcards.viewModel.CardInfoForUserViewModel;
import com.example.bankcards.viewModel.CardSearchViewModel;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.hibernate.query.spi.Limit;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CardFacade {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final CardBlockRequestRepository cardBlockRequestRepository;
    private final CardFactory cardFactory;
    private final SimpleDateFormat cardDateOfExpirationDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final int CARD_SEARCH_LIMIT = 20;

    @NotNull
    public List<CardSearchViewModel> searchByUser(@NotNull final String username, @NotNull final CardSearchDTO cardSearchDTO) throws NotCriticalExceptionAbstract {
        if(username == null || username.trim().isEmpty()){
            throw new UserNotFoundException();
        }
        final Optional<User> user = this.userRepository.findByUsername(username);
        if(user.isEmpty()){
            throw new UserNotFoundException();
        }

        List<CardSearchViewModel> cardSearchViewModels = new ArrayList<>();
        final int skip = cardSearchDTO.getPage() > 0 ? (cardSearchDTO.getPage() - 1) * CARD_SEARCH_LIMIT : 0;
        final int maxRows = cardSearchDTO.getPage() > 0 ? cardSearchDTO.getPage() * CARD_SEARCH_LIMIT : CARD_SEARCH_LIMIT;
        final List<Card> cards = this.cardRepository.searchByQueryAndUserId(
                cardSearchDTO.getSearchQuery(),
                user.get().getId(),
                new Limit(skip, maxRows)
        );
        if(!cards.isEmpty()){
            cardSearchViewModels = cards.stream().map(card -> this.cardFactory.createSearch(card)).toList();
        }

        return cardSearchViewModels;
    }

    /**
     * Получение информации по карте для пользователя по идентификатору
     * @param idStr
     * @return
     * @throws CardNotFoundException
     */
    @NotNull
    public CardInfoForUserViewModel getInfoForUserByIdStr(@NotNull final String idStr) throws CardNotFoundException {
        Long id = 0L;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e){
            throw new CardNotFoundException();
        }
        return this.getInfoForUserById(id);
    }

    @NotNull
    public CardInfoForUserViewModel getInfoForUserById(@NotNull final Long id) throws CardNotFoundException {
        final Optional<Card> card = this.cardRepository.findById(id);
        if(card.isEmpty()){
            throw new CardNotFoundException();
        }
        return this.cardFactory.createInfoForUser(card.get());
    }

    @NotNull
    public List<CardSearchViewModel> searchByAdmin(@NotNull final CardSearchDTO cardSearchDTO){
        List<CardSearchViewModel> cardSearchViewModels = new ArrayList<>();
        final int skip = cardSearchDTO.getPage() > 0 ? (cardSearchDTO.getPage() - 1) * CARD_SEARCH_LIMIT : 0;
        final int maxRows = cardSearchDTO.getPage() > 0 ? cardSearchDTO.getPage() * CARD_SEARCH_LIMIT : CARD_SEARCH_LIMIT;
        final List<Card> cards = this.cardRepository.searchByQuery(
                cardSearchDTO.getSearchQuery(),
                new Limit(skip, maxRows)
        );
        if(!cards.isEmpty()){
            cardSearchViewModels = cards.stream().map(card -> this.cardFactory.createSearch(card)).toList();
        }

        return cardSearchViewModels;
    }

    public void deleteByIdStr(@NotNull final String idStr) throws NotCriticalExceptionAbstract{
        Long id = 0L;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e){
            throw new CardNotFoundException();
        }
        this.deleteById(id);
    }

    public void deleteById(@NotNull final Long id){
        this.cardRepository.deleteById(id);
    }

    /**
     * Редактирование данных карты
     * @param idStr идентификатор карты в виде строки
     * @param cardEditByAdminDTO
     * @throws NotCriticalExceptionAbstract
     */
    public void editByAdmin(@NotNull final String idStr, @NotNull final CardEditByAdminDTO cardEditByAdminDTO) throws NotCriticalExceptionAbstract {
        Long id = 0L;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e){
            throw new CardNotFoundException();
        }
        this.editByAdmin(id, cardEditByAdminDTO);
    }

    /**
     * Редактирование данных карты
     * @param id идентификатор карты в виде типа данных Long
     * @param cardEditByAdminDTO
     * @throws CardNotFoundException
     */
    @Transactional
    public void editByAdmin(@NotNull final Long id, @NotNull final CardEditByAdminDTO cardEditByAdminDTO) throws NotCriticalExceptionAbstract {
        final Optional<Card> card = this.cardRepository.findById(id);
        if(card.isEmpty()){
            throw new CardNotFoundException();
        }

        boolean isAnyChanges = false;
        CardStatus cardStatusEdit;
        try {
            cardStatusEdit = CardStatus.valueOf(cardEditByAdminDTO.getStatus());
        } catch (IllegalArgumentException e){
            throw new CardEditFailedException("значение status не корректно");
        }
        if(cardStatusEdit != card.get().getStatus()){
            card.get().setStatus(cardStatusEdit);
            isAnyChanges = true;
            if(card.get().getUser() != null && cardStatusEdit == CardStatus.BLOCKED){
                final Optional<CardBlockRequest> cardBlockRequest = this.cardBlockRequestRepository.findByUserIdAndCardId(
                        card.get().getUser().getId(),
                        card.get().getId()
                );
                if(cardBlockRequest.isPresent() && cardBlockRequest.get().getDateOfBlocked() == null){
                    cardBlockRequest.get().setDateOfBlocked(new Date());
                    this.cardBlockRequestRepository.save(cardBlockRequest.get());
                }
            }
        }

        //Здесь можно редактировать другие поля

        if(isAnyChanges){
            card.get().setDateOfUpdate(new Date());
            this.cardRepository.save(card.get());
        }
    }

    /**
     * Получение информации по карте для администратора по id карты
     * @param idStr
     * @return
     * @throws CardNotFoundException
     */
    @NotNull
    public CardInfoForAdminViewModel getInfoById(@NotNull final String idStr) throws NotCriticalExceptionAbstract {
        Long id = 0L;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e){
            throw new CardNotFoundException();
        }

        final Optional<Card> card = this.cardRepository.findById(id);
        if(card.isEmpty()){
            throw new CardNotFoundException();
        }
        return this.cardFactory.createInfoForAdmin(card.get());
    }

//    /**
//     * Получение информации по карте для администратора по номеру карты
//     * @param cardNumber
//     * @return
//     * @throws CardNotFoundException
//     */
//    @NotNull
//    public CardInfoForAdminViewModel getInfoByCardNumber(@NotNull final String cardNumber) throws CardNotFoundException {
//        Optional<Card> card = this.cardRepository.findByNumber(cardNumber);
//        if(card.isEmpty()){
//            throw new CardNotFoundException();
//        }
//        return this.cardFactory.createInfoForAdmin(card.get());
//    }

    /**
     * Создание новой карточки для пользователя
     * @param cardNewDTO
     * @return
     * @throws NotCriticalExceptionAbstract
     */
    @NotNull
    public CardInfoForAdminViewModel createForUser(@NotNull final CardNewDTO cardNewDTO) throws NotCriticalExceptionAbstract {
        if(cardNewDTO.getUserUsername() == null || cardNewDTO.getUserUsername().trim().isEmpty()){
            throw new CardCreateFailedException("нет username пользователя");
        }
        if(cardNewDTO.getCardNumber() == null || cardNewDTO.getCardNumber().trim().isEmpty()){
            throw new CardCreateFailedException("нет номера карты");
        }
        String cardNewNumber = cardNewDTO.getCardNumber().trim().replace(" ", "");
        if(cardNewNumber.length() != 16){
            throw new CardCreateFailedException("длина номера карты не соответствует 16 символам");
        }

        if(cardNewDTO.getCardDateOfExpiration() == null || cardNewDTO.getCardDateOfExpiration().trim().isEmpty()){
            throw new CardCreateFailedException("нет даты окончания действия карты");
        }

        Date dateOfExpiration = null;
        try {
            dateOfExpiration = this.cardDateOfExpirationDateFormat.parse(cardNewDTO.getCardDateOfExpiration());
        } catch (ParseException e) {
            throw new CardCreateFailedException("дата окончания действия карты не соответвует формату 2000-01-01");
        }

        final Optional<User> user = this.userRepository.findByUsername(cardNewDTO.getUserUsername());
        if(user.isEmpty()){
            throw new UserNotFoundException();
        }
        if(this.cardRepository.findByNumber(cardNewNumber).isPresent()){
            throw new CardCreateFailedException("данный номер уже существует в базе данных");
        }

        final Card card = new Card();
        card.setUser(user.get());
        card.setNumber(cardNewNumber);
        card.setDateOfExpiration(dateOfExpiration);
        card.setBalance(new BigDecimal("0.00"));
        card.setStatus(CardStatus.ACTIVE);

        final Date dateNow = new Date();
        card.setDateOfAdd(dateNow);
        card.setDateOfUpdate(dateNow);

        this.cardRepository.save(card);

        return this.cardFactory.createInfoForAdmin(card);
    }
}
