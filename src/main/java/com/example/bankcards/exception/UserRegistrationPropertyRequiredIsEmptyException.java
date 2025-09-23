package com.example.bankcards.exception;

import com.example.bankcards.abstracts.NotCriticalExceptionAbstract;
import jakarta.validation.constraints.NotNull;

public class UserRegistrationPropertyRequiredIsEmptyException extends NotCriticalExceptionAbstract {
    public UserRegistrationPropertyRequiredIsEmptyException(@NotNull final String propertyName) {
        super(String.format("При регистрации пользователя произошла ошибка, не получено свойство \"%s\"", propertyName));
    }
}
