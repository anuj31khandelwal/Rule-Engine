package com.engine.rule.validation;

import java.util.HashMap;
import java.util.Map;

public class AttributeValidationService {

    private final Map<String, Validator<?>> validators = new HashMap<>();

    public AttributeValidationService() {
        validators.put("age", new AgeValidator());
        validators.put("department", new DepartmentValidator());
        validators.put("income", new IncomeValidator());
        validators.put("spend", new SpendValidator());
    }

    public boolean validateAttribute(String attributeName, Object value) {
        Validator<Object> validator = (Validator<Object>) validators.get(attributeName);
        return validator != null && validator.isValid(value);
    }

    public boolean validateAttributes(Map<String, Object> attributes) {
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            if (!validateAttribute(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        return true;
    }
}