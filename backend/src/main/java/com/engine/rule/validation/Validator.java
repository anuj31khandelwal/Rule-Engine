package com.engine.rule.validation;

public interface Validator<T> {
    boolean isValid(T value);
}