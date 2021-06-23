package com.nowakowski.bartlomiej.budget.assistant.controller;

import com.nowakowski.bartlomiej.budget.assistant.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("budget/assistant")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping(value = "/{amount}")
    public ResponseEntity<String> recharge(@PathVariable Double amount) {
        if (isNegativeNumber(amount)) {
            return ResponseEntity.badRequest().build();
        }
        budgetService.recharge(amount);
        return ResponseEntity.ok().build();
    }

    private boolean isNegativeNumber(Double amount) {
        return amount < 0;
    }
}
