package com.example.bankcards.exception;

import com.example.bankcards.abstracts.NotCriticalExceptionAbstract;

public class CardInfoWrongException extends NotCriticalExceptionAbstract {
    public CardInfoWrongException(String message) {
        super(String.format("Ошибка найдена при проверке данных карты: %s", message));
    }
}
