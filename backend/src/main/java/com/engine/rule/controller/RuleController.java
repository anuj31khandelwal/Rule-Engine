package com.engine.rule.controller;

import com.engine.rule.entity.Node;
import com.engine.rule.repository.NodeRepository;
import com.engine.rule.validation.RuleValidator.InvalidRuleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.engine.rule.service.RuleService;

@RestController
@RequestMapping("/api/rules")
public class RuleController {

    @Autowired
    private RuleService ruleService;

    @Autowired
    private NodeRepository nodeRepository;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("API is working");
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createRule(@RequestBody String rule) {
        try {
            Node createdRule = ruleService.createRule(rule);
            return ResponseEntity.ok(createdRule);
        } catch (InvalidRuleException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid rule: " + e.getMessage());
        }
    }

    @PostMapping("/combine")
    public ResponseEntity<Node> combineRules(@RequestBody List<String> rules, @RequestParam String operator) {
        // Convert the string-based rules into Node objects
        List<Node> ruleNodes = rules.stream()
                .map(rule -> {
                    try {
                        return ruleService.createRule(rule);
                    } catch (InvalidRuleException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        // Combine the nodes with the provided operator
        Node combinedRule = ruleService.combineRules(ruleNodes, operator);

        // Return the combined rule
        return ResponseEntity.ok(combinedRule);
    }


    @PostMapping("/evaluate")
    public ResponseEntity<Boolean> evaluateRule(@RequestBody Map<String, Object> data) {
        boolean result = ruleService.evaluateRule(data);
        return ResponseEntity.ok(result);
    }
    @PutMapping("/updateOperator/{nodeId}")
    public ResponseEntity<String> updateOperator(@PathVariable Long nodeId, @RequestParam String newOperator) {
        ruleService.updateOperator(nodeId, newOperator);
        return ResponseEntity.ok("Operator updated successfully");
    }

    @PostMapping("/addSubExpression")
    public ResponseEntity<String> addSubExpression(@RequestParam Long parentId, @RequestParam Long childId, @RequestParam boolean isLeft) {
        ruleService.addSubExpression(parentId, childId, isLeft);
        return ResponseEntity.ok("Sub-expression added successfully");
    }

}