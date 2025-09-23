package com.example.bankcards.facade;

import com.example.bankcards.dto.CardTransferNewDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardTransferFailedException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.model.CardStatus;
import com.example.bankcards.model.Role;
import com.example.bankcards.repository.CardBlockRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.CardTransferRepository;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Optional;

@SpringBootTest
@EnableAutoConfiguration(exclude = {
        LiquibaseAutoConfiguration.class,
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
public class CardTransferFacadeTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CardRepository cardRepository;

    @MockBean
    private CardTransferRepository cardTransferRepository;

    @MockBean
    private CardBlockRequestRepository cardBlockRequestRepository;

    @Autowired
    private CardTransferFacade cardTransferFacade;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final SimpleDateFormat simpleDateFormatDateOfAdd = new SimpleDateFormat("yyyy-MM-dd");

    private User userMock;
    private User userMockAnother;
    private Card cardFrom;
    private Card cardTo;

    @BeforeEach
    public void init() throws Exception {
        this.userMock = new User();
        this.userMock.setId(1L);
        this.userMock.setUsername("username");
        this.userMock.setPassword(this.bCryptPasswordEncoder.encode("password"));
        this.userMock.setRole(Role.USER);
        Mockito.when(this.userRepository.findByUsername("username")).thenReturn(Optional.of(this.userMock));
        Mockito.when(this.userRepository.findById(1L)).thenReturn(Optional.of(this.userMock));

        this.userMockAnother = new User();
        this.userMockAnother.setId(2L);
        this.userMockAnother.setUsername("username2");
        this.userMockAnother.setPassword(this.bCryptPasswordEncoder.encode("password2"));
        this.userMockAnother.setRole(Role.USER);
        Mockito.when(this.userRepository.findByUsername("username2")).thenReturn(Optional.of(this.userMockAnother));
        Mockito.when(this.userRepository.findById(2L)).thenReturn(Optional.of(this.userMockAnother));

        this.cardFrom = new Card();
        this.cardFrom.setId(1L);
        this.cardFrom.setNumber("1234567812345678");
        this.cardFrom.setUser(userMock);
        this.cardFrom.setBalance(BigDecimal.valueOf(500L));
        this.cardFrom.setDateOfExpiration(simpleDateFormatDateOfAdd.parse("2099-01-01"));
        Mockito.when(this.cardRepository.findById(1L)).thenReturn(Optional.of(this.cardFrom));

        this.cardTo = new Card();
        this.cardTo.setId(2L);
        this.cardTo.setNumber("1234567812340000");
        this.cardTo.setUser(userMock);
        this.cardTo.setBalance(BigDecimal.valueOf(100L));
        this.cardTo.setDateOfExpiration(simpleDateFormatDateOfAdd.parse("2099-01-01"));
        Mockito.when(this.cardRepository.findById(2L)).thenReturn(Optional.of(this.cardTo));
    }

    @Test
    public void createForOwnCardsTest() throws Exception {
        Assertions.assertEquals(
                "user not found",
                Assertions.assertThrows(
                        UserNotFoundException.class,
                        () -> this.cardTransferFacade.createForOwnCards("username_wrong",
                                new CardTransferNewDTO(
                                        0L,
                                        0L,
                                        BigDecimal.valueOf(100)
                                ))
                ).getMessage()
        );
        Assertions.assertEquals(
                "Ошибка при попытке транзакции: карта-источник не найдена",
                Assertions.assertThrows(
                        CardTransferFailedException.class,
                        () -> this.cardTransferFacade.createForOwnCards("username",
                                new CardTransferNewDTO(
                                        0L,
                                        0L,
                                        BigDecimal.valueOf(100)
                                ))
                ).getMessage()
        );

        this.cardFrom.setUser(null);
        Assertions.assertEquals(
                "Ошибка при попытке транзакции: карта-источник не имеет владельца",
                Assertions.assertThrows(
                        CardTransferFailedException.class,
                        () -> this.cardTransferFacade.createForOwnCards("username",
                                new CardTransferNewDTO(
                                        1L,
                                        0L,
                                        BigDecimal.valueOf(100)
                                ))
                ).getMessage()
        );
        this.cardFrom.setUser(this.userMockAnother);
        Assertions.assertEquals(
                "Ошибка при попытке транзакции: карта-источник не принадлежит пользователю",
                Assertions.assertThrows(
                        CardTransferFailedException.class,
                        () -> this.cardTransferFacade.createForOwnCards("username",
                                new CardTransferNewDTO(
                                        1L,
                                        0L,
                                        BigDecimal.valueOf(100)
                                ))
                ).getMessage()
        );
        this.cardFrom.setUser(this.userMock);

        this.cardFrom.setStatus(CardStatus.EXPIRED);
        Assertions.assertEquals(
                "Ошибка при попытке транзакции: карта-источник истек срок обслуживания",
                Assertions.assertThrows(
                        CardTransferFailedException.class,
                        () -> this.cardTransferFacade.createForOwnCards("username",
                                new CardTransferNewDTO(
                                        1L,
                                        0L,
                                        BigDecimal.valueOf(100)
                                ))
                ).getMessage()
        );
        this.cardFrom.setStatus(CardStatus.ACTIVE);

        this.cardFrom.setStatus(CardStatus.BLOCKED);
        Assertions.assertEquals(
                "Ошибка при попытке транзакции: карта-источник заблокирована",
                Assertions.assertThrows(
                        CardTransferFailedException.class,
                        () -> this.cardTransferFacade.createForOwnCards("username",
                                new CardTransferNewDTO(
                                        1L,
                                        0L,
                                        BigDecimal.valueOf(100)
                                ))
                ).getMessage()
        );
        this.cardFrom.setStatus(CardStatus.ACTIVE);

        this.cardFrom.setBalance(BigDecimal.valueOf(50L));
        Assertions.assertEquals(
                "Ошибка при попытке транзакции: недостаточно средств",
                Assertions.assertThrows(
                        CardTransferFailedException.class,
                        () -> this.cardTransferFacade.createForOwnCards("username",
                                new CardTransferNewDTO(
                                        1L,
                                        0L,
                                        BigDecimal.valueOf(100)
                                ))
                ).getMessage()
        );
        this.cardFrom.setBalance(BigDecimal.valueOf(500L));



        Assertions.assertEquals(
                "Ошибка при попытке транзакции: карта-назначение не найдена",
                Assertions.assertThrows(
                        CardTransferFailedException.class,
                        () -> this.cardTransferFacade.createForOwnCards("username",
                                new CardTransferNewDTO(
                                        1L,
                                        0L,
                                        BigDecimal.valueOf(100)
                                ))
                ).getMessage()
        );

        this.cardTo.setUser(null);
        Assertions.assertEquals(
                "Ошибка при попытке транзакции: карта-назначение не имеет владельца",
                Assertions.assertThrows(
                        CardTransferFailedException.class,
                        () -> this.cardTransferFacade.createForOwnCards("username",
                                new CardTransferNewDTO(
                                        1L,
                                        2L,
                                        BigDecimal.valueOf(100)
                                ))
                ).getMessage()
        );
        this.cardTo.setUser(this.userMockAnother);
        Assertions.assertEquals(
                "Ошибка при попытке транзакции: карта-назначение не принадлежит пользователю",
                Assertions.assertThrows(
                        CardTransferFailedException.class,
                        () -> this.cardTransferFacade.createForOwnCards("username",
                                new CardTransferNewDTO(
                                        1L,
                                        2L,
                                        BigDecimal.valueOf(100)
                                ))
                ).getMessage()
        );
        this.cardTo.setUser(this.userMock);

        this.cardTo.setStatus(CardStatus.EXPIRED);
        Assertions.assertEquals(
                "Ошибка при попытке транзакции: карта-назначение истек срок обслуживания",
                Assertions.assertThrows(
                        CardTransferFailedException.class,
                        () -> this.cardTransferFacade.createForOwnCards("username",
                                new CardTransferNewDTO(
                                        1L,
                                        2L,
                                        BigDecimal.valueOf(100)
                                ))
                ).getMessage()
        );
        this.cardTo.setStatus(CardStatus.ACTIVE);

        this.cardTo.setStatus(CardStatus.BLOCKED);
        Assertions.assertEquals(
                "Ошибка при попытке транзакции: карта-назначение заблокирована",
                Assertions.assertThrows(
                        CardTransferFailedException.class,
                        () -> this.cardTransferFacade.createForOwnCards("username",
                                new CardTransferNewDTO(
                                        1L,
                                        2L,
                                        BigDecimal.valueOf(100)
                                ))
                ).getMessage()
        );
        this.cardTo.setStatus(CardStatus.BLOCKED);
    }
}
