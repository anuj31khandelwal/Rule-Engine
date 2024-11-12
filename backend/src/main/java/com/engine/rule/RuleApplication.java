package com.engine.rule;

import com.engine.rule.validation.AttributeValidationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class RuleApplication {

	public static void main(String[] args) {
		SpringApplication.run(RuleApplication.class, args);
		AttributeValidationService validationService = new AttributeValidationService();

		Map<String, Object> attributes = new HashMap<>();
		attributes.put("age", 25);
		attributes.put("department", "Engineering");
		attributes.put("income", 50000.0);
		attributes.put("spend", 30000.0);

		boolean isValid = validationService.validateAttributes(attributes);
		if (isValid) {
			System.out.println("All attributes are valid and can be added to the catalog.");
		} else {
			System.out.println("Some attributes are invalid. Please check the input values.");
		}
	}

}
