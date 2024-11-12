package com.engine.rule.entity;

public class RuleEvaluationRequest {
    private int age;
    private String department;
    private double salary;
    private int experience;

    // Constructors, Getters, Setters
    public RuleEvaluationRequest() {}

    public RuleEvaluationRequest(int age, String department, double salary, int experience) {
        this.age = age;
        this.department = department;
        this.salary = salary;
        this.experience = experience;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}
