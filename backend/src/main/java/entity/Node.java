package entity;

public class Node {
    private String type;  // "operator" or "operand"
    private Node left;
    private Node right;
    private String value;  // for operand nodes

    public Node() {
    }

    // Constructor to accept both type and value
    public Node(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public Node(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String toString() {
        return "Node{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", left=" + (left != null ? left.toString() : "null") +
                ", right=" + (right != null ? right.toString() : "null") +
                '}';
    }
    // Getters and setters

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
