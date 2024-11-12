package com.engine.rule.validation;

public class AgeValidator implements Validator<Integer> {
    @Override
    public boolean isValid(Integer age) {
        return age != null && age >= AttributeConstraints.MIN_AGE && age <= AttributeConstraints.MAX_AGE;
    }
}