package com.example.bankcards.exception;

import com.example.bankcards.abstracts.NotCriticalExceptionAbstract;

public class CardEditFailedException extends NotCriticalExceptionAbstract {
    public CardEditFailedException(String message) {
        super(String.format("Ошибка при попытке редактирования данные карты: %s", message));
    }
}
