package com.example.bankcards.abstracts;

import jakarta.validation.constraints.NotNull;

public class EntityNotFoundExceptionAbstract extends NotCriticalExceptionAbstract{
    public EntityNotFoundExceptionAbstract(@NotNull final String entityName) {
        super(String.format("%s not found", entityName));
    }
}
