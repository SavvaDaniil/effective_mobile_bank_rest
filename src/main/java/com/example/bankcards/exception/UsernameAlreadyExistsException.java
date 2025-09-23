package com.example.bankcards.exception;

import com.example.bankcards.abstracts.NotCriticalExceptionAbstract;
import jakarta.validation.constraints.NotNull;

public class UsernameAlreadyExistsException extends NotCriticalExceptionAbstract {
    public UsernameAlreadyExistsException(@NotNull final String username) {
        super(String.format("Пользователь с username \"%s\" уже существует", username));
    }
}
