package com.example.bankcards.exception;

import com.example.bankcards.abstracts.NotCriticalExceptionAbstract;

public class UserEditFailedException extends NotCriticalExceptionAbstract {
    public UserEditFailedException(String message) {
        super(String.format("При редактировании данных пользователя произошла ошибка: %s", message));
    }
}
