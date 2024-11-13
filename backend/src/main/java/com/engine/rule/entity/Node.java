package com.engine.rule.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "nodes")
public class Node {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String value;

    // Mapping to other nodes
    @OneToOne
    private Node left;

    @OneToOne
    private Node right;

    private String operator;
    // E.g., "AND", "OR", ">", "<"
    private String operand;  // Holds operand values if it’s a leaf node

    public Node() {
    }

    // Constructor to accept both type and value
    public Node(String type, String value) {
        this.type = type;
        this.value = value;
    }


    @Override
    public String toString() {
        return "Node{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", left=" + (left != null ? left.toString() : "null") +
                ", right=" + (right != null ? right.toString() : "null") +
                '}';
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
    public void setOperator(String newOperator) {
        this.operator = newOperator;
    }

    public void setOperand(String newOperand) {
        this.operand = newOperand;
    }

    public void addLeftChild(Node child) {
        this.left = child;
    }

    public void addRightChild(Node child) {
        this.right = child;
    }

    public void removeChild(boolean isLeft) {
        if (isLeft) this.left = null;
        else this.right = null;
    }

}
