package com.example.bankcards.exception;

import com.example.bankcards.abstracts.NotCriticalExceptionAbstract;

public class CardBlockRequestCreateFailedException extends NotCriticalExceptionAbstract {
    public CardBlockRequestCreateFailedException(String message) {
        super(String.format("Ошибка при попытке создания запроса на блокировку карты: %s", message));
    }
}
