package com.engine.rule.service;

import com.engine.rule.entity.Node;

public class RuleParser {

    public Node parseRule(String ruleString) {
        // Removing white spaces
        ruleString = ruleString.replaceAll("\\s+", "");

        return parseExpression(ruleString);
    }

    private Node parseExpression(String ruleString) {
        // Simple recursive logic for parsing AND, OR, NOT expressions
        if (ruleString.contains("AND")) {
            String[] parts = ruleString.split("AND", 2);
            Node node = new Node("operator", "AND");
            node.setLeft(parseExpression(parts[0]));
            node.setRight(parseExpression(parts[1]));
            return node;
        } else if (ruleString.contains("OR")) {
            String[] parts = ruleString.split("OR", 2);
            Node node = new Node("operator", "OR");
            node.setLeft(parseExpression(parts[0]));
            node.setRight(parseExpression(parts[1]));
            return node;
        } else if (ruleString.startsWith("NOT")) {
            Node node = new Node("operator", "NOT");
            node.setLeft(parseExpression(ruleString.substring(3)));  // Parse expression after NOT
            return node;
        } else if (ruleString.startsWith("(") && ruleString.endsWith(")")) {
            return parseExpression(ruleString.substring(1, ruleString.length() - 1)); // Removing parentheses
        } else {
            // This is an operand (like "A", "B", etc.)
            return new Node("operand", ruleString);
        }
    }
}
