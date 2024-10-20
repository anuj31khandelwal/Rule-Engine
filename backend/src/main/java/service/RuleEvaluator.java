package entity;

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
        if (condition.contains("age")) {
            int ageValue = Integer.parseInt(condition.split(">")[1].trim());
            return data.getAge() > ageValue;
        } else if (condition.contains("department")) {
            String department = condition.split("=")[1].trim().replace("'", "");
            return data.getDepartment().equals(department);
        } else if (condition.contains("salary")) {
            double salaryValue = Double.parseDouble(condition.split(">")[1].trim());
            return data.getSalary() > salaryValue;
        } else if (condition.contains("experience")) {
            int experienceValue = Integer.parseInt(condition.split(">")[1].trim());
            return data.getExperience() > experienceValue;
        }

        return false; // Default case if no condition matches
    }
}
