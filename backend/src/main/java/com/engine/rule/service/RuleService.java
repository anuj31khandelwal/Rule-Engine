package com.engine.rule.service;

import com.engine.rule.entity.Node;
import com.engine.rule.repository.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class RuleService {

    @Autowired
    private NodeRepository nodeRepository;

    // Method to parse and create a rule as a Node tree
    public Node createRule(String rule) {
        System.out.println("Parsing rule: " + rule);
        Node rootNode = parseRule(rule);
        saveNodeTree(rootNode); // Recursively saves the node tree in the database
        return rootNode;
    }

    // Parses the rule string into a Node tree
    private Node parseRule(String rule) {
        System.out.println("Tokenizing rule: " + rule);
        List<String> tokens = tokenize(rule);
        System.out.println("Tokens: " + tokens);
        Node rootNode = parseExpression(tokens);
        return rootNode;
    }

    // Recursively save the node tree to the database
    private void saveNodeTree(Node node) {
        if (node == null) return;

        System.out.println("Saving node: " + node);
        if (node.getLeft() != null) {
            saveNodeTree(node.getLeft());
        }

        if (node.getRight() != null) {
            saveNodeTree(node.getRight());
        }

        nodeRepository.save(node);
    }

    // Tokenize the rule string into a list of operators and operands
    private List<String> tokenize(String rule) {
        // Splits rule string by spaces and keeps parentheses as separate tokens
        List<String> tokens = Arrays.stream(rule.split("(?<=\\s|\\()|(?=\\s|\\))"))
                .filter(token -> !token.isBlank())
                .collect(Collectors.toList());
        System.out.println("Tokenized rule: " + tokens);
        return tokens;
    }

    private Node parseExpression(List<String> tokens) {
        Node rootNode = null;
        Node currentNode = null;

        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);

            // Handle logical operators like AND, OR
            if (token.equals("AND") || token.equals("OR")) {
                if (currentNode == null) {
                    throw new IllegalArgumentException("Logical operator found without a left operand");
                }
                // Set the logical operator on the current node
                currentNode.setOperand(token); // We are setting the logical operator on the node
            } else {
                // It must be a condition (attribute, operator, value)
                if (i + 2 < tokens.size()) {  // Ensure there are enough tokens for a condition (attribute, operator, value)
                    String attribute = token;
                    String operator = tokens.get(i + 1);
                    String value = tokens.get(i + 2);

                    // Validate and create the condition node
                    Node conditionNode = parseSimpleCondition(attribute, operator, value);

                    if (currentNode == null) {
                        // If no current node, make this the root node
                        currentNode = conditionNode;
                        rootNode = currentNode;
                    } else {
                        // Link the new condition node to the existing one using left/right child relationship
                        currentNode.setRight(conditionNode); // Or setLeft based on logical flow
                        currentNode = conditionNode; // Move the current node to the new condition node
                    }
                    i += 2; // Skip the next two tokens (operator, value)
                } else {
                    throw new IllegalArgumentException("Invalid condition format: " + token);
                }
            }
        }

        return rootNode;
    }
    private Node parseSimpleCondition(String attribute, String operator, String value) {
        // Validate the operator and value format
        if (attribute == null || operator == null || value == null) {
            throw new IllegalArgumentException("Invalid condition format: Missing attribute, operator, or value");
        }

        // Create a simple condition node
        Node conditionNode = new Node();
        conditionNode.setType("condition");  // Set the type of the node to "condition"
        conditionNode.setName(attribute);    // Set the field name (e.g., "age")
        conditionNode.setOperator(operator); // Set the comparison operator (e.g., ">", "=")
        conditionNode.setValue(value);       // Set the value to be compared (e.g., "25")

        return conditionNode;
    }


    // Method to determine operator priority (optional)
    private int getOperatorPriority(String token) {
        if (token.equals("AND") || token.equals("OR")) return 1;
        if (token.equals(">") || token.equals("<") || token.equals("=")) return 2;
        return Integer.MAX_VALUE;
    }

    // Evaluates the combined rule with the provided data map
    public boolean evaluateRule(Map<String, Object> data) {
        String rule = "age > 25 AND department = 'Sales' AND experience > 3";
        return evaluateCondition(rule, data);
    }
    public Node combineRules(List<Node> rules, String operator) {
        if (rules == null || rules.isEmpty()) {
            throw new IllegalArgumentException("Rules cannot be null or empty.");
        }

        // Initialize the root node as the first rule
        Node rootNode = rules.get(0);
        Node currentNode = rootNode;

        // Combine rules using the specified operator
        for (int i = 1; i < rules.size(); i++) {
            Node nextNode = rules.get(i);
            Node newParentNode = new Node();
            newParentNode.setLeft(currentNode);
            newParentNode.setRight(nextNode);
            newParentNode.setType("logical");
            newParentNode.setValue(operator); // Set the logical operator (AND/OR)

            // Update the current node to be the new combined node
            currentNode = newParentNode;
        }

        return currentNode; // Return the combined rule as a tree
    }

    // Evaluates conditions joined by AND
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

    public void updateOperator(Long nodeId, String newOperator) {
        Node node = nodeRepository.findById(nodeId).orElseThrow();
        node.setOperator(newOperator);
        nodeRepository.save(node);
    }

    public void addSubExpression(Long parentId, Long childId, boolean isLeft) {
        Node parentNode = nodeRepository.findById(parentId).orElseThrow();
        Node childNode = nodeRepository.findById(childId).orElseThrow();
        if (isLeft) {
            parentNode.addLeftChild(childNode);
        } else {
            parentNode.addRightChild(childNode);
        }
        nodeRepository.save(parentNode);
    }
}
