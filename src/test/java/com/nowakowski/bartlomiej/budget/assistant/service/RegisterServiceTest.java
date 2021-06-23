package com.nowakowski.bartlomiej.budget.assistant.service;

import com.nowakowski.bartlomiej.budget.assistant.entity.Budget;
import com.nowakowski.bartlomiej.budget.assistant.entity.Register;
import com.nowakowski.bartlomiej.budget.assistant.entity.RegisterType;
import com.nowakowski.bartlomiej.budget.assistant.repository.BudgetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class RegisterServiceTest {

    @Mock
    public BudgetRepository budgetRepository;

    @InjectMocks
    public BudgetService systemUnderTest;

    @Captor
    public ArgumentCaptor<Budget> budgetCaptor;

    public static final Double ANY_AMOUNT = 0.0;

    @Test
    public void shouldRechargeWallet() {
        Double amountToRecharge = 500.0;
        List<Budget> budget = prepareBudget();
        when(budgetRepository.findAll()).thenReturn(budget);

        systemUnderTest.recharge(amountToRecharge);

        verify(budgetRepository).save(budgetCaptor.capture());
        Budget rechargedBudget = budgetCaptor.getValue();
        assertEquals(amountToRecharge, rechargedBudget.getWallet().getValue());
        assertEquals(ANY_AMOUNT, rechargedBudget.getSavings().getValue());
        assertEquals(ANY_AMOUNT, rechargedBudget.getInsurancePolicy().getValue());
        assertEquals(ANY_AMOUNT, rechargedBudget.getFoodExpenses().getValue());
    }

    @Test
    public void shouldNotRechargeWalletWhenNoBudget() {
        when(budgetRepository.findAll()).thenReturn(List.of());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> systemUnderTest.recharge(ANY_AMOUNT));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("No budget defined", exception.getReason());
    }

    private List<Budget> prepareBudget() {
        Register wallet = new Register(RegisterType.WALLET, ANY_AMOUNT);
        Register savings = new Register(RegisterType.SAVINGS, ANY_AMOUNT);
        Register insurance = new Register(RegisterType.INSURANCE_POLICY, ANY_AMOUNT);
        Register food = new Register(RegisterType.FOOD_EXPENSES, ANY_AMOUNT);
        Budget budget = new Budget(wallet, savings, insurance, food);
        return List.of(budget);
    }
}
