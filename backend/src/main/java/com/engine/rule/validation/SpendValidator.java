package com.engine.rule.validation;

public class SpendValidator implements Validator<Double> {
    @Override
    public boolean isValid(Double spend) {
        return spend != null && spend <= AttributeConstraints.MAX_SPEND;
    }
}