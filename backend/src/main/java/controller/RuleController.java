package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rules")
public class RuleController {

    @Autowired
    private RuleService ruleService;

    @PostMapping("/create")
    public ResponseEntity<String> createRule(@RequestBody String rule) {
        String createdRule = ruleService.createRule(rule);
        return ResponseEntity.ok(createdRule);
    }

    @PostMapping("/combine")
    public ResponseEntity<String> combineRules(@RequestBody List<String> rules) {
        String combinedRule = ruleService.combineRules(rules);
        return ResponseEntity.ok(combinedRule);
    }

    @PostMapping("/evaluate")
    public ResponseEntity<Boolean> evaluateRule(@RequestBody Map<String, Object> data) {
        boolean result = ruleService.evaluateRule(data);
        return ResponseEntity.ok(result);
    }
}