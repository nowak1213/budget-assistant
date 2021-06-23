package com.nowakowski.bartlomiej.budget.assistant;

import com.nowakowski.bartlomiej.budget.assistant.entity.Budget;
import com.nowakowski.bartlomiej.budget.assistant.entity.Register;
import com.nowakowski.bartlomiej.budget.assistant.entity.RegisterType;
import com.nowakowski.bartlomiej.budget.assistant.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final BudgetRepository budgetRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (budgetRepository.count() == 0) {
            Register wallet = new Register(RegisterType.WALLET, 1000.0);
            Register savings = new Register(RegisterType.SAVINGS, 5000.0);
            Register insurance = new Register(RegisterType.INSURANCE_POLICY, 0.0);
            Register food = new Register(RegisterType.FOOD_EXPENSES, 0.0);
            Budget budget = new Budget(wallet, savings, insurance, food);
            budgetRepository.save(budget);
        }
    }
}
