package com.engine.rule.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RuleService {

    // Method to clean up the rule string
    public String createRule(String rule) {
        return rule.replaceAll("^\"|\"$", "");
    }

    // Method to combine multiple rules, wrapping each in parentheses for clarity
    public String combineRules(List<String> rules) {
        return rules.stream()
                .map(rule -> "(" + rule.replaceAll("^\"|\"$", "") + ")")
                .collect(Collectors.joining(" AND "));
    }

    // Evaluates the combined rule with the provided data map
    public boolean evaluateRule(Map<String, Object> data) {
        String rule = "age > 25 AND department = 'Sales' AND experience > 3";
        return evaluateCondition(rule, data);
    }

    // Splits and evaluates conditions joined by AND
    private boolean evaluateCondition(String condition, Map<String, Object> data) {
        String[] parts = condition.split(" AND ");
        for (String part : parts) {
            if (!evaluateSimpleCondition(part.trim(), data)) {
                return false;
            }
        }
        return true;
    }

    // Evaluates a single condition with detailed parsing and debug outputs
    private boolean evaluateSimpleCondition(String condition, Map<String, Object> data) {
        // Split the condition into its components
        String[] parts = condition.split(" ");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid condition format: " + condition);
        }

        String key = parts[0];
        String operator = parts[1];
        String value = parts[2].replace("'", ""); // Clean quotes from the rule value

        // Retrieve and process the data value corresponding to the key
        Object dataValue = data.get(key);
        if (dataValue == null) {
            System.out.println("Data key not found: " + key);
            return false;
        }

        // Debugging output for tracing values
        System.out.println("Evaluating condition: " + condition);
        System.out.println("Data value for key " + key + ": " + dataValue);
        System.out.println("Rule value to compare: " + value);

        // If dataValue is a string, remove any extra quotes
        if (dataValue instanceof String) {
            dataValue = ((String) dataValue).replace("'", "");
        }

        // Handle comparison based on the data type and operator
        switch (operator) {
            case ">":
                // Verify that dataValue is numeric before comparing
                if (dataValue instanceof Number) {
                    return ((Number) dataValue).doubleValue() > Double.parseDouble(value);
                } else {
                    System.out.println("Error: Non-numeric data for '>' comparison");
                    return false;
                }
            case "<":
                if (dataValue instanceof Number) {
                    return ((Number) dataValue).doubleValue() < Double.parseDouble(value);
                } else {
                    System.out.println("Error: Non-numeric data for '<' comparison");
                    return false;
                }
            case "=":
                // Compare strings directly after removing quotes
                return dataValue.toString().equals(value);
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }
}
