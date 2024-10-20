# Rule-Engine

Objective
This is a simple 3-tier rule engine application designed to determine user eligibility based on attributes such as age, department, income, and spend. The system leverages an Abstract Syntax Tree (AST) to dynamically create, combine, and modify rules.

Features
Define conditional rules and store them as AST.
Combine multiple rules into a single AST to improve efficiency.
Evaluate rules against user data in JSON format.
Data Structure
A Node structure is used to represent the AST with the following fields:

type: Node type ("operator" for AND/OR, "operand" for conditions)
left: Left child Node
right: Right child Node
value: Optional value for operand nodes (e.g., number for comparisons)
Sample Rules
Rule 1: ((age > 30 AND department = 'Sales') OR (age < 25 AND department = 'Marketing')) AND (salary > 50000 OR experience > 5)
Rule 2: ((age > 30 AND department = 'Marketing')) AND (salary > 20000 OR experience > 5)
API Endpoints
POST /create_rule
Create a rule by sending a rule string as input.
Example Request:

json
///
{
  "rule_string": "age > 30 AND department = 'Sales'"
}
///
POST /combine_rules
Combine multiple rules into a single AST.
Example Request:

json
///
{
  "rules": ["rule1", "rule2"]
}
///
POST /evaluate_rule
Evaluate a rule against provided user data.
Example Request:

json
Copy code
{
  "rule": "combined_rule_ast",
  "data": {
    "age": 35,
    "department": "Sales",
    "salary": 60000,
    "experience": 3
  }
}
Dependencies
Java
Spring Boot
JSON libraries
Database (choose as per preference for storing rules, e.g., MySQL, MongoDB)
Setup Instructions
Clone the repository:
bash
Copy code
git clone <repository-url>
cd rule-engine-ast
Install dependencies:
bash
Copy code
mvn install
Set up the database and configure credentials in application.properties.
Run the application:
bash
Copy code
mvn spring-boot:run
