package com.nowakowski.bartlomiej.budget.assistant.service;

import com.nowakowski.bartlomiej.budget.assistant.BudgetDTO;
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

    public BudgetDTO getBalance(){
        Budget budget = getBudget();
        return mapBudgetToDTO(budget);
    }

    private Budget getBudget() {
        Iterator<Budget> budgetIterator = budgetRepository.findAll().iterator();
        if (budgetIterator.hasNext()) {
            return budgetIterator.next();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No budget defined");
        }
    }

    private BudgetDTO mapBudgetToDTO(Budget budget) {
        Double walletAmount = budget.getWallet().getValue();
        Double savingsAmount = budget.getSavings().getValue();
        Double insurancePolicy = budget.getInsurancePolicy().getValue();
        Double foodExpenses = budget.getFoodExpenses().getValue();
        return new BudgetDTO(walletAmount, savingsAmount, insurancePolicy, foodExpenses);
    }
}
