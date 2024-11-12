package com.engine.rule.validation;

public class DepartmentValidator implements Validator<String> {
    @Override
    public boolean isValid(String department) {
        return department != null && AttributeConstraints.VALID_DEPARTMENTS.contains(department);
    }
}
