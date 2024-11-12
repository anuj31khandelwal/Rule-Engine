package com.engine.rule.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RuleService {

    public String createRule(String rule) {
        // Remove any surrounding quotes if present
        return rule.replaceAll("^\"|\"$", "");
    }

    public String combineRules(List<String> rules) {
        // Join rules with AND, removing any surrounding quotes
        return rules.stream()
                .map(rule -> rule.replaceAll("^\"|\"$", ""))
                .collect(Collectors.joining(" AND "));
    }

    public boolean evaluateRule(Map<String, Object> data) {
        // For simplicity, we'll use the combined rule from the previous example
        String rule = "age > 30 AND department = 'Sales'";
        return evaluateCondition(rule, data);
    }

    private boolean evaluateCondition(String condition, Map<String, Object> data) {
        String[] parts = condition.split(" AND ");
        for (String part : parts) {
            if (!evaluateSimpleCondition(part.trim(), data)) {
                return false;
            }
        }
        return true;
    }

    private boolean evaluateSimpleCondition(String condition, Map<String, Object> data) {
        String[] parts = condition.split(" ");
        String key = parts[0];
        String operator = parts[1];
        String value = parts[2].replace("'", "");

        Object dataValue = data.get(key);
        if (dataValue == null) {
            return false;
        }

        switch (operator) {
            case ">":
                return ((Number) dataValue).doubleValue() > Double.parseDouble(value);
            case "<":
                return ((Number) dataValue).doubleValue() < Double.parseDouble(value);
            case "=":
                return dataValue.toString().equals(value);
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }
}