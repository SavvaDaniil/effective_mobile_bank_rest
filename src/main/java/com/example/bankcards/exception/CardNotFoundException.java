package com.example.bankcards.exception;

import com.example.bankcards.abstracts.EntityNotFoundExceptionAbstract;

public class CardNotFoundException extends EntityNotFoundExceptionAbstract {
    public CardNotFoundException() {
        super("card");
    }
}
