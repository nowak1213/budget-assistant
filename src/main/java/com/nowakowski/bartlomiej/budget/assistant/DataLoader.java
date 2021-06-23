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

    public static final Double INITIAL_WALLET_VALUE = 1000.0;
    public static final Double INITIAL_SAVINGS_VALUE = 5000.0;
    public static final Double INITIAL_INSURANCE_POLICY_VALUE = 0.0;
    public static final Double INITIAL_FOOD_EXPENSES_VALUE = 0.0;

    @Override
    public void run(ApplicationArguments args) {
        if (budgetRepository.count() == 0) {
            Register wallet = new Register(RegisterType.WALLET, INITIAL_WALLET_VALUE);
            Register savings = new Register(RegisterType.SAVINGS, INITIAL_SAVINGS_VALUE);
            Register insurance = new Register(RegisterType.INSURANCE_POLICY, INITIAL_INSURANCE_POLICY_VALUE);
            Register food = new Register(RegisterType.FOOD_EXPENSES, INITIAL_FOOD_EXPENSES_VALUE);
            Budget budget = new Budget(wallet, savings, insurance, food);
            budgetRepository.save(budget);
        }
    }
}
