package com.example.bankcards.facade;

import com.example.bankcards.abstracts.NotCriticalExceptionAbstract;
import com.example.bankcards.dto.UserEditByAdminDTO;
import com.example.bankcards.dto.UserSigninDTO;
import com.example.bankcards.dto.UserRegistrationDTO;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.*;
import com.example.bankcards.factory.UserFactory;
import com.example.bankcards.model.Role;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.JwtUtil;
import com.example.bankcards.viewModel.UserInfoForAdminViewModel;
import com.example.bankcards.viewModel.UserProfileLiteViewModel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@AllArgsConstructor
public class UserFacade {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserFactory userFactory;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Удаление пользователя по id
     * @param userIdStr
     * @throws UserNotFoundException
     */
    public void deleteUserByAdmin(@NotNull final String userIdStr) throws UserNotFoundException {
        Long userId = 0L;
        try {
            userId = Long.parseLong(userIdStr);
        } catch (NumberFormatException e){
            throw new UserNotFoundException();
        }

        final Optional<User> user = this.userRepository.findById(userId);
        if(user.isEmpty()){
            throw new UserNotFoundException();
        }

        this.userRepository.delete(user.get());
    }

    /**
     * Редактирование профиля пользователя администратором
     * @param userEditByAdminDTO
     * @throws NotCriticalExceptionAbstract
     */
    public void editUserByAdmin(@NotNull final UserEditByAdminDTO userEditByAdminDTO) throws NotCriticalExceptionAbstract {
        if(userEditByAdminDTO.getId() == 0){
            throw new UserEditFailedException("id is empty");
        }
        if(userEditByAdminDTO.getUsername() == null || userEditByAdminDTO.getUsername().trim().isEmpty()){
            throw new UserEditFailedException("username is empty");
        }

        final Optional<User> user = this.userRepository.findById(userEditByAdminDTO.getId());
        if(user.isEmpty()){
            throw new UserNotFoundException();
        }

        boolean isAnyChanges = false;
        if(!user.get().getUsername().equals(userEditByAdminDTO.getUsername().trim())){
            final String usernameNew = userEditByAdminDTO.getUsername().trim();
            if(this.userRepository.existsByUsernameAndIdNot(usernameNew, user.get().getId())){
                throw new UsernameAlreadyExistsException(userEditByAdminDTO.getUsername());
            }
            user.get().setUsername(usernameNew);
            isAnyChanges = true;
        }

        //Сюда можно добавить другие поля

        if(isAnyChanges){
            user.get().setDateOfUpdate(new Date());
            this.userRepository.save(user.get());
        }
    }

//    /**
//     * Редактирование профиля пользователя пользователем
//     * @param userUsername
//     * @param userEditByUserDTO
//     * @throws NotCriticalExceptionAbstract
//     */
//    public void editByUser(@NotNull final String userUsername, @NotNull final UserEditByUserDTO userEditByUserDTO) throws NotCriticalExceptionAbstract {
//        Optional<User> user = this.userRepository.findByUsername(userUsername);
//        if(user.isEmpty()){
//            throw new UserNotFoundException();
//        }
//
//        if(userEditByUserDTO.getUsername() == null || userEditByUserDTO.getUsername().trim().isEmpty()){
//            throw new UserEditFailedException("username is empty");
//        }
//
//        boolean isAnyChanges = false;
//        if(!user.get().getUsername().equals(userEditByUserDTO.getUsername().trim())){
//            final String usernameNew = userEditByUserDTO.getUsername().trim();
//            if(this.userRepository.existsByUsernameAndIdNot(usernameNew, user.get().getId())){
//                throw new UsernameAlreadyExistsException(userEditByUserDTO.getUsername());
//            }
//            user.get().setUsername(usernameNew);
//            isAnyChanges = true;
//        }
//
//        //Сюда можно добавить другие поля
//
//        if(isAnyChanges){
//            user.get().setDateOfUpdate(new Date());
//            this.userRepository.save(user.get());
//        }
//    }

    @NotNull
    public UserInfoForAdminViewModel getInfoForAdminByIdStr(@NotNull final String userIdStr) throws UserNotFoundException {
        Long userId = 0L;
        try {
            userId = Long.parseLong(userIdStr);
        } catch (NumberFormatException e){
            throw new UserNotFoundException();
        }
        return this.getInfoForAdminById(userId);
    }

    @NotNull
    public UserInfoForAdminViewModel getInfoForAdminById(@NotNull final Long userId) throws UserNotFoundException {
        final Optional<User> user = this.userRepository.findById(userId);
        if(user.isEmpty()){
            throw new UserNotFoundException();
        }
        return this.userFactory.createInfoForAdmin(user.get());
    }

    @NotNull
    public UserProfileLiteViewModel getProfileByUsername(@NotNull final String username) throws NotCriticalExceptionAbstract {
        final Optional<User> user = this.userRepository.findByUsername(username);
        if(user.isEmpty()){
            throw new UserNotFoundException();
        }
        return this.userFactory.createProfile(user.get());
    }

    /**
     * Аутентификация пользователя
     * @param userSigninDTO
     * @return
     * @throws NotCriticalExceptionAbstract
     */
    @NotNull
    public String signin(@NotNull final UserSigninDTO userSigninDTO) throws NotCriticalExceptionAbstract {
        if(userSigninDTO.getUsername() == null || userSigninDTO.getUsername().trim().isEmpty()
            || userSigninDTO.getPassword() == null || userSigninDTO.getPassword().trim().isEmpty()){
            throw new UserAuthFailedException();
        }

        final Optional<User> user = this.userRepository.findByUsername(userSigninDTO.getUsername());
        if(user.isEmpty()){
            throw new UserAuthFailedException();
        }

        if(!this.bCryptPasswordEncoder.matches(userSigninDTO.getPassword(), user.get().getPassword())){
            throw new UserAuthFailedException();
        }

        return this.jwtUtil.generateToken(user.get().getUsername());
    }

    /**
     * Регистрация нового пользователя
     * @param userRegistrationDTO
     * @return
     * @throws NotCriticalExceptionAbstract
     */
    @NotNull
    public String registration(@NotNull final UserRegistrationDTO userRegistrationDTO) throws NotCriticalExceptionAbstract {
        if(userRegistrationDTO.getUsername() == null || userRegistrationDTO.getUsername().trim().isEmpty()){
            throw new UserRegistrationPropertyRequiredIsEmptyException("username");
        }

        if(this.userRepository.findByUsername(userRegistrationDTO.getUsername()).isPresent()){
            throw new UsernameAlreadyExistsException(userRegistrationDTO.getUsername());
        }

        if(userRegistrationDTO.getPassword() == null || userRegistrationDTO.getPassword().trim().isEmpty()){
            throw new UserRegistrationPropertyRequiredIsEmptyException("password");
        }

        final User user = new User();
        user.setUsername(userRegistrationDTO.getUsername());
        user.setPassword(this.passwordEncoder.encode(userRegistrationDTO.getPassword()));
        user.setRole(userRegistrationDTO.isAdmin() ? Role.ADMIN : Role.USER);
        final Date dateNow = new Date();
        user.setDateOfAdd(dateNow);
        user.setDateOfUpdate(dateNow);

        this.userRepository.save(user);

        return this.jwtUtil.generateToken(user.getUsername());
    }
}
