package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RuleEvaluator {

    public boolean evaluate(Node ruleNode, String jsonData) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode data = objectMapper.readTree(jsonData);
        return evaluateNode(ruleNode, data);
    }

    private boolean evaluateNode(Node node, JsonNode data) {
        if ("operator".equals(node.getType())) {
            switch (node.getValue()) {
                case "AND":
                    return evaluateNode(node.getLeft(), data) && evaluateNode(node.getRight(), data);
                case "OR":
                    return evaluateNode(node.getLeft(), data) || evaluateNode(node.getRight(), data);
                case "NOT":
                    return !evaluateNode(node.getLeft(), data);
                default:
                    throw new IllegalArgumentException("Unknown operator: " + node.getValue());
            }
        } else if ("operand".equals(node.getType())) {
            return evaluateOperand(node.getValue(), data);
        }
        throw new IllegalArgumentException("Invalid node type: " + node.getType());
    }

    private boolean evaluateOperand(String operand, JsonNode data) {
        String[] parts = operand.split("(?<=[<>=])(?=[^=])|(?<=[^=])(?=[<>=])");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid operand format: " + operand);
        }

        String field = parts[0].trim();
        String operator = parts[1].trim();
        String value = parts[2].trim();

        JsonNode fieldValue = data.get(field);
        if (fieldValue == null) {
            return false;
        }

        switch (operator) {
            case ">":
                return fieldValue.asDouble() > Double.parseDouble(value);
            case "<":
                return fieldValue.asDouble() < Double.parseDouble(value);
            case ">=":
                return fieldValue.asDouble() >= Double.parseDouble(value);
            case "<=":
                return fieldValue.asDouble() <= Double.parseDouble(value);
            case "=":
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    return fieldValue.asText().equals(value.substring(1, value.length() - 1));
                } else {
                    return fieldValue.asText().equals(value);
                }
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }
}