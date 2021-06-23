package com.nowakowski.bartlomiej.budget.assistant.service;

import com.nowakowski.bartlomiej.budget.assistant.entity.Budget;
import com.nowakowski.bartlomiej.budget.assistant.entity.Register;
import com.nowakowski.bartlomiej.budget.assistant.entity.RegisterType;
import com.nowakowski.bartlomiej.budget.assistant.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Iterator;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;

    public void recharge(Double amount) {
        Budget budget = getBudget();
        Register rechargedWallet = new Register(RegisterType.WALLET, budget.getWallet().getValue() + amount);
        budget.setWallet(rechargedWallet);
        budgetRepository.save(budget);
    }

    private Budget getBudget() {
        Iterator<Budget> budgetIterator = budgetRepository.findAll().iterator();
        if (budgetIterator.hasNext()) {
            return budgetIterator.next();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No budget defined");
        }
    }
}
