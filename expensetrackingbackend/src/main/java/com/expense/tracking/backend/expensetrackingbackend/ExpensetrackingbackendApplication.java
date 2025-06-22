package com.expense.tracking.backend.expensetrackingbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ExpensetrackingbackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpensetrackingbackendApplication.class, args);
	}

}
