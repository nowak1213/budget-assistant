package com.nowakowski.bartlomiej.budget.assistant;

import com.nowakowski.bartlomiej.budget.assistant.entity.Budget;
import com.nowakowski.bartlomiej.budget.assistant.repository.BudgetRepository;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DataLoaderTest {

    @Autowired
    public BudgetRepository budgetRepository;

    @Test
    public void testInitBudgetRepo () {
        Budget initialBudget = budgetRepository.findById(1L).get();

        assertEquals(1, budgetRepository.count());
        assertEquals(1000.0, initialBudget.getWallet().getValue());
        assertEquals(5000.0, initialBudget.getSavings().getValue());
        assertEquals(0.0, initialBudget.getInsurancePolicy().getValue());
        assertEquals(0.0, initialBudget.getFoodExpenses().getValue());
    }
}
