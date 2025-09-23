package com.example.bankcards.abstracts;

/**
 * Критические ошибки, содержимое нельзя отображать пользователям
 */
public abstract class CriticalExceptionAbstract extends Exception {
    protected CriticalExceptionAbstract(String message){
        super(message);
    }
}
