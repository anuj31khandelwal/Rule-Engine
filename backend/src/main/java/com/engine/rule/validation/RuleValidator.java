package com.engine.rule.validation;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RuleValidator {

    // Define allowed operators and attributes
    private static final Set<String> OPERATORS = Set.of(">", "<", ">=", "<=", "==", "!=");
    private static final Set<String> VALID_ATTRIBUTES = Set.of("age", "department", "experience");

    public void validateRule(String rule) throws InvalidRuleException {
        if (!hasValidOperators(rule)) {
            throw new InvalidRuleException("Rule contains invalid or missing operators.");
        }
        if (!hasBalancedParentheses(rule)) {
            throw new InvalidRuleException("Unmatched parentheses in rule.");
        }
        if (!hasAttributeForComparisons(rule)) {
            throw new InvalidRuleException("Comparisons must include an attribute on the left side.");
        }
        if (!areAttributesValid(rule)) {
            throw new InvalidRuleException("Invalid attribute used in rule.");
        }
        if (!hasValidLogicalOperators(rule)) {
            throw new InvalidRuleException("Logical operators (AND, OR) must be used correctly.");
        }
        if (hasConsecutiveLogicalOperators(rule)) {
            throw new InvalidRuleException("Rule contains consecutive logical operators.");
        }
    }

    private boolean hasValidOperators(String rule) {
        // Ensure that each segment contains at least one valid operator
        String[] segments = rule.split("AND|OR");
        for (String segment : segments) {
            boolean containsOperator = false;
            for (String operator : OPERATORS) {
                if (segment.contains(operator)) {
                    containsOperator = true;
                    break;
                }
            }
            if (!containsOperator) return false;
        }
        return true;
    }

    private boolean hasBalancedParentheses(String rule) {
        int balance = 0;
        for (char c : rule.toCharArray()) {
            if (c == '(') balance++;
            else if (c == ')') balance--;
            if (balance < 0) return false;
        }
        return balance == 0;
    }

    private boolean hasAttributeForComparisons(String rule) {
        // Regex to match comparisons with missing attribute (e.g., "> 25")
        Pattern pattern = Pattern.compile("(^|\\s)([><=!]=?|!=)\\s*\\d+");
        Matcher matcher = pattern.matcher(rule);
        return !matcher.find(); // If matcher finds a match, rule is invalid
    }

    private boolean areAttributesValid(String rule) {
        // Extract all attributes and ensure they are valid
        Pattern pattern = Pattern.compile("([a-zA-Z_][a-zA-Z0-9_]*)\\s*(?:[><=!]=?|!=)\\s*");
        Matcher matcher = pattern.matcher(rule);

        while (matcher.find()) {
            String attribute = matcher.group(1); // Extract the attribute
            if (!VALID_ATTRIBUTES.contains(attribute)) {
                return false; // Invalid attribute detected
            }
        }
        return true;
    }

    private boolean hasValidLogicalOperators(String rule) {
        // Ensure the logical operators are properly used, i.e., no leading or trailing 'AND'/'OR'
        return !(rule.startsWith("AND") || rule.startsWith("OR") || rule.endsWith("AND") || rule.endsWith("OR"));
    }

    private boolean hasConsecutiveLogicalOperators(String rule) {
        // Check if there are consecutive logical operators
        Pattern pattern = Pattern.compile("\\b(AND|OR)\\s+(AND|OR)\\b");
        Matcher matcher = pattern.matcher(rule);
        return matcher.find();
    }

    // Custom exception
    public static class InvalidRuleException extends Exception {
        public InvalidRuleException(String message) {
            super(message);
        }
    }
}
