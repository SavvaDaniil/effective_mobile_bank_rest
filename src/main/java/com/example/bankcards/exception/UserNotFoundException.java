package com.example.bankcards.exception;

import com.example.bankcards.abstracts.EntityNotFoundExceptionAbstract;

public class UserNotFoundException extends EntityNotFoundExceptionAbstract {
    public UserNotFoundException() {
        super("user");
    }
}
