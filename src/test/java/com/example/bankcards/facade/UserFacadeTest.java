package com.example.bankcards.facade;

import com.example.bankcards.dto.UserEditByAdminDTO;
import com.example.bankcards.dto.UserRegistrationDTO;
import com.example.bankcards.dto.UserSigninDTO;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.*;
import com.example.bankcards.model.Role;
import com.example.bankcards.repository.CardBlockRequestRepository;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.CardTransferRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.viewModel.UserInfoForAdminViewModel;
import com.example.bankcards.viewModel.UserProfileLiteViewModel;
import io.jsonwebtoken.lang.Assert;
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

import java.util.Optional;

@SpringBootTest
@EnableAutoConfiguration(exclude = {
        LiquibaseAutoConfiguration.class,
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
public class UserFacadeTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CardRepository cardRepository;

    @MockBean
    private CardTransferRepository cardTransferRepository;

    @MockBean
    private CardBlockRequestRepository cardBlockRequestRepository;

    @Autowired
    private UserFacade userFacade;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void init(){
        User userMock = new User();
        userMock.setId(1L);
        userMock.setUsername("username");
        userMock.setPassword(this.bCryptPasswordEncoder.encode("password"));
        userMock.setRole(Role.USER);
        Mockito.when(this.userRepository.findByUsername("username")).thenReturn(Optional.of(userMock));
        Mockito.when(this.userRepository.findById(1L)).thenReturn(Optional.of(userMock));
    }

    @Test
    public void deleteUserByAdminTest() throws Exception {
        Assertions.assertEquals(
                "user not found",
                Assertions.assertThrows(
                        UserNotFoundException.class,
                        () -> this.userFacade.deleteUserByAdmin("wrong_long")
                ).getMessage()
        );
        Assertions.assertEquals(
                "user not found",
                Assertions.assertThrows(
                        UserNotFoundException.class,
                        () -> this.userFacade.deleteUserByAdmin("2")
                ).getMessage()
        );
        this.userFacade.deleteUserByAdmin("1");
    }

    @Test
    public void editUserByAdminTest() throws Exception {
        Assertions.assertEquals(
                "При редактировании данных пользователя произошла ошибка: id is empty",
                Assertions.assertThrows(
                        UserEditFailedException.class,
                        () -> this.userFacade.editUserByAdmin(new UserEditByAdminDTO(0L, "username"))
                ).getMessage()
        );
        Assertions.assertEquals(
                "При редактировании данных пользователя произошла ошибка: username is empty",
                Assertions.assertThrows(
                        UserEditFailedException.class,
                        () -> this.userFacade.editUserByAdmin(new UserEditByAdminDTO(1L, null))
                ).getMessage()
        );
        Assertions.assertEquals(
                "При редактировании данных пользователя произошла ошибка: username is empty",
                Assertions.assertThrows(
                        UserEditFailedException.class,
                        () -> this.userFacade.editUserByAdmin(new UserEditByAdminDTO(1L, ""))
                ).getMessage()
        );
        Assertions.assertEquals(
                "user not found",
                Assertions.assertThrows(
                        UserNotFoundException.class,
                        () -> this.userFacade.editUserByAdmin(new UserEditByAdminDTO(2L, "username_wrong"))
                ).getMessage()
        );

        this.userFacade.editUserByAdmin(new UserEditByAdminDTO(1L, "username_new"));
    }

    @Test
    public void getInfoForAdminByIdStrTest() throws Exception {
        Assertions.assertEquals(
                "user not found",
                Assertions.assertThrows(
                        UserNotFoundException.class,
                        () -> this.userFacade.getInfoForAdminByIdStr("string_here")
                ).getMessage()
        );
        Assertions.assertEquals(
                "user not found",
                Assertions.assertThrows(
                        UserNotFoundException.class,
                        () -> this.userFacade.getInfoForAdminByIdStr("0")
                ).getMessage()
        );
        UserInfoForAdminViewModel userInfoForAdminViewModel = this.userFacade.getInfoForAdminByIdStr("1");
        Assert.notNull(userInfoForAdminViewModel);
        Assert.notNull(userInfoForAdminViewModel.getUsername());
        Assertions.assertFalse(userInfoForAdminViewModel.getUsername().trim().isEmpty());
    }

    @Test
    public void getProfileByUsernameTest() throws Exception {
        Assertions.assertEquals(
                "user not found",
                Assertions.assertThrows(
                        UserNotFoundException.class,
                        () -> this.userFacade.getProfileByUsername("username_wrong")
                ).getMessage()
        );

        UserProfileLiteViewModel userProfileLiteViewModel = this.userFacade.getProfileByUsername("username");
        Assert.notNull(userProfileLiteViewModel);
        Assert.notNull(userProfileLiteViewModel.getUsername());
        Assertions.assertFalse(userProfileLiteViewModel.getUsername().trim().isEmpty());
    }

    @Test
    public void signinTest() throws Exception {
        Assertions.assertEquals(
                "wrong",
                Assertions.assertThrows(
                        UserAuthFailedException.class,
                        () -> this.userFacade.signin(new UserSigninDTO(
                                null,
                                null
                        ))
                ).getMessage()
        );
        Assertions.assertEquals(
                "wrong",
                Assertions.assertThrows(
                        UserAuthFailedException.class,
                        () -> this.userFacade.signin(new UserSigninDTO(
                                "",
                                null
                        ))
                ).getMessage()
        );
        Assertions.assertEquals(
                "wrong",
                Assertions.assertThrows(
                        UserAuthFailedException.class,
                        () -> this.userFacade.signin(new UserSigninDTO(
                                "username",
                                null
                        ))
                ).getMessage()
        );
        Assertions.assertEquals(
                "wrong",
                Assertions.assertThrows(
                        UserAuthFailedException.class,
                        () -> this.userFacade.signin(new UserSigninDTO(
                                "username",
                                ""
                        ))
                ).getMessage()
        );
        Assertions.assertEquals(
                "wrong",
                Assertions.assertThrows(
                        UserAuthFailedException.class,
                        () -> this.userFacade.signin(new UserSigninDTO(
                                "wrong_username",
                                "password"
                        ))
                ).getMessage()
        );

        Assertions.assertEquals(
                "wrong",
                Assertions.assertThrows(
                        UserAuthFailedException.class,
                        () -> this.userFacade.signin(new UserSigninDTO(
                                "username",
                                "wrong_password"
                        ))
                ).getMessage()
        );
        Assert.notNull(this.userFacade.signin(new UserSigninDTO(
                "username",
                "password"
        )));
    }

    @Test
    public void registrationTest() throws Exception {
        Assertions.assertEquals(
                "При регистрации пользователя произошла ошибка, не получено свойство \"username\"",
                Assertions.assertThrows(
                        UserRegistrationPropertyRequiredIsEmptyException.class,
                        () -> this.userFacade.registration(new UserRegistrationDTO(
                                null,
                                null,
                                false
                        ))
                ).getMessage()
        );
        Assertions.assertEquals(
                "При регистрации пользователя произошла ошибка, не получено свойство \"username\"",
                Assertions.assertThrows(
                        UserRegistrationPropertyRequiredIsEmptyException.class,
                        () -> this.userFacade.registration(new UserRegistrationDTO(
                                "",
                                null,
                                false
                        ))
                ).getMessage()
        );
        Assertions.assertEquals(
                "При регистрации пользователя произошла ошибка, не получено свойство \"password\"",
                Assertions.assertThrows(
                        UserRegistrationPropertyRequiredIsEmptyException.class,
                        () -> this.userFacade.registration(new UserRegistrationDTO(
                                "username_new",
                                null,
                                false
                        ))
                ).getMessage()
        );
        Assertions.assertEquals(
                "При регистрации пользователя произошла ошибка, не получено свойство \"password\"",
                Assertions.assertThrows(
                        UserRegistrationPropertyRequiredIsEmptyException.class,
                        () -> this.userFacade.registration(new UserRegistrationDTO(
                                "username_new",
                                "",
                                false
                        ))
                ).getMessage()
        );
        Assertions.assertEquals(
                "Пользователь с username \"username\" уже существует",
                Assertions.assertThrows(
                        UsernameAlreadyExistsException.class,
                        () -> this.userFacade.registration(new UserRegistrationDTO(
                                "username",
                                "password",
                                false
                        ))
                ).getMessage()
        );

        Assert.notNull(this.userFacade.registration(new UserRegistrationDTO(
                "username_new",
                "password",
                false
        )));
    }
}
