package com.engine.rule.validation;

public class IncomeValidator implements Validator<Double> {
    @Override
    public boolean isValid(Double income) {
        return income != null && income >= AttributeConstraints.MIN_INCOME && income <= AttributeConstraints.MAX_INCOME;
    }
}
