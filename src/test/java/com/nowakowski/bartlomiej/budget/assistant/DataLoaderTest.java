package com.nowakowski.bartlomiej.budget.assistant;

import com.nowakowski.bartlomiej.budget.assistant.entity.Budget;
import com.nowakowski.bartlomiej.budget.assistant.repository.BudgetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;

import static com.nowakowski.bartlomiej.budget.assistant.DataLoader.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataLoaderTest {

    @Mock
    public BudgetRepository budgetRepository;

    @InjectMocks
    public DataLoader systemUnderTest;

    @Captor
    public ArgumentCaptor<Budget> budgetCaptor;

    @Test
    public void shouldSaveInitialBudget() {
        systemUnderTest.run(mock(ApplicationArguments.class));

        verify(budgetRepository).save(budgetCaptor.capture());
        Budget rechargedBudget = budgetCaptor.getValue();
        assertEquals(INITIAL_WALLET_VALUE, rechargedBudget.getWallet().getValue());
        assertEquals(INITIAL_SAVINGS_VALUE, rechargedBudget.getSavings().getValue());
        assertEquals(INITIAL_INSURANCE_POLICY_VALUE, rechargedBudget.getInsurancePolicy().getValue());
        assertEquals(INITIAL_FOOD_EXPENSES_VALUE, rechargedBudget.getFoodExpenses().getValue());
    }

    @Test
    public void shouldNotSaveInitialBudget() {
        when(budgetRepository.count()).thenReturn(1L);

        systemUnderTest.run(mock(ApplicationArguments.class));

        verify(budgetRepository, times(0)).save(any());
    }
}
