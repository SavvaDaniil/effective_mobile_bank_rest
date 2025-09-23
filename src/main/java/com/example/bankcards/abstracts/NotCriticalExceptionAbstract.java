package com.example.bankcards.abstracts;

/**
 * Некритические ошибки, текст ошибок предназначен для пользователей
 */
public abstract class NotCriticalExceptionAbstract extends Exception {
    protected NotCriticalExceptionAbstract(String message){
        super(message);
    }
}
