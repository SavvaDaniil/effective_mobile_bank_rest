package com.example.bankcards.exception;

import com.example.bankcards.abstracts.NotCriticalExceptionAbstract;

public class CardTransferFailedException extends NotCriticalExceptionAbstract {
    public CardTransferFailedException(final String message) {
        super(String.format("Ошибка при попытке транзакции: %s", message));
    }
}
