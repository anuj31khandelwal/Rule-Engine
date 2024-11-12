package com.engine.rule.service;

import com.engine.rule.entity.Node;
import com.engine.rule.entity.RuleEvaluationRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleEvaluator {

    // Evaluate the AST recursively against the user data
    public boolean evaluate(Node root, RuleEvaluationRequest data) {
        if (root == null) {
            return false;
        }

        if (root.getType().equals("operand")) {
            // Evaluate the operand condition
            return evaluateCondition(root.getValue(), data);
        }

        boolean leftResult = evaluate(root.getLeft(), data);
        boolean rightResult = evaluate(root.getRight(), data);

        // Apply the operator (AND or OR)
        if (root.getValue().equals("AND")) {
            return leftResult && rightResult;
        } else if (root.getValue().equals("OR")) {
            return leftResult || rightResult;
        }

        return false;
    }

    // Evaluates the condition (e.g., "age > 30") against the user data
    private boolean evaluateCondition(String condition, RuleEvaluationRequest data) {
        Pattern pattern = Pattern.compile("(\\w+)\\s*(>|<|=|>=|<=|!=)\\s*([\\w']+)");
        Matcher matcher = pattern.matcher(condition);

        if (matcher.matches()) {
            String attribute = matcher.group(1);
            String operator = matcher.group(2);
            String value = matcher.group(3);

            switch (attribute) {
                case "age":
                    return evaluateNumericCondition(data.getAge(), operator, Integer.parseInt(value));
                case "salary":
                    return evaluateNumericCondition(data.getSalary(), operator, Double.parseDouble(value));
                case "experience":
                    return evaluateNumericCondition(data.getExperience(), operator, Integer.parseInt(value));
                case "department":
                    return evaluateStringCondition(data.getDepartment(), operator, value.replace("'", ""));
                default:
                    return false; // Unsupported attribute
            }
        }

        return false; // Condition not matching expected format
    }

    private boolean evaluateNumericCondition(double actualValue, String operator, double targetValue) {
        switch (operator) {
            case ">":
                return actualValue > targetValue;
            case "<":
                return actualValue < targetValue;
            case "=":
                return actualValue == targetValue;
            case ">=":
                return actualValue >= targetValue;
            case "<=":
                return actualValue <= targetValue;
            case "!=":
                return actualValue != targetValue;
            default:
                return false;
        }
    }

    private boolean evaluateStringCondition(String actualValue, String operator, String targetValue) {
        if (operator.equals("=")) {
            return actualValue.equals(targetValue);
        } else if (operator.equals("!=")) {
            return !actualValue.equals(targetValue);
        }
        return false;
    }

}