package com.example.bankcards.exception;

import com.example.bankcards.abstracts.NotCriticalExceptionAbstract;

public class CardCreateFailedException extends NotCriticalExceptionAbstract {
    public CardCreateFailedException(String message) {
        super(String.format("При попытке создания карты возникла ошибка: %s", message));
    }
}
