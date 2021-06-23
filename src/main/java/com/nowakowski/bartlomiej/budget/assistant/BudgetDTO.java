package com.nowakowski.bartlomiej.budget.assistant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BudgetDTO {
    private final Double walletAmount;
    private final Double savingsAmount;
    private final Double insurancePolicyAmount;
    private final Double foodExpensesAmount;
}
