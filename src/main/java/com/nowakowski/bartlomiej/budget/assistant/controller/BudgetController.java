package com.nowakowski.bartlomiej.budget.assistant.controller;

import com.nowakowski.bartlomiej.budget.assistant.BudgetDTO;
import com.nowakowski.bartlomiej.budget.assistant.entity.RegisterType;
import com.nowakowski.bartlomiej.budget.assistant.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("budget/assistant")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping(value = "/recharge/{amount}")
    public ResponseEntity<String> recharge(@PathVariable Double amount) {
        if (isNegativeNumber(amount)) {
            return ResponseEntity.badRequest().build();
        }
        budgetService.recharge(amount);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<BudgetDTO> getBalance() {
        BudgetDTO result = budgetService.getBalance();
        return ResponseEntity.ok().body(result);
    }

    @PostMapping(value = "/transfer/{amount}")
    public ResponseEntity<String> transfer(@PathVariable Double amount, @RequestParam RegisterType from,
                                           @RequestParam RegisterType to) {
        if (isNegativeNumber(amount)) {
            return ResponseEntity.badRequest().build();
        }
        budgetService.transfer(amount, from, to);
        return ResponseEntity.ok().build();
    }

    private boolean isNegativeNumber(Double amount) {
        return amount < 0;
    }
}
