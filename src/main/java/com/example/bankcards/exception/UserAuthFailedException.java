package com.example.bankcards.exception;

import com.example.bankcards.abstracts.NotCriticalExceptionAbstract;

public class UserAuthFailedException extends NotCriticalExceptionAbstract {
    public UserAuthFailedException() {
        super("wrong");
    }
}
