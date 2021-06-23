package com.nowakowski.bartlomiej.budget.assistant.service;

import com.nowakowski.bartlomiej.budget.assistant.BudgetDTO;
import com.nowakowski.bartlomiej.budget.assistant.entity.Budget;
import com.nowakowski.bartlomiej.budget.assistant.entity.Register;
import com.nowakowski.bartlomiej.budget.assistant.entity.RegisterType;
import com.nowakowski.bartlomiej.budget.assistant.repository.BudgetRepository;
import com.nowakowski.bartlomiej.budget.assistant.repository.RegisterRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BudgetServiceTest {

    @Mock
    public BudgetRepository budgetRepository;

    @Mock
    public RegisterRepository registerRepository;

    @InjectMocks
    public BudgetService systemUnderTest;

    @Captor
    public ArgumentCaptor<Budget> budgetCaptor;

    @Captor
    public ArgumentCaptor<Register> registerCaptor;

    public static final Double ANY_WALLET_AMOUNT = 100.0;
    public static final Double ANY_SAVINGS_AMOUNT = 200.0;
    public static final Double ANY_INSURANCE_POLICY_AMOUNT = 300.0;
    public static final Double ANY_FOOD_EXPENSES_AMOUNT = 400.0;

    @Test
    public void shouldRechargeWallet() {
        Double amountToRecharge = 500.0;
        List<Budget> budget = prepareBudget();
        when(budgetRepository.findAll()).thenReturn(budget);

        systemUnderTest.recharge(amountToRecharge);

        verify(budgetRepository).save(budgetCaptor.capture());
        Budget rechargedBudget = budgetCaptor.getValue();
        assertEquals(amountToRecharge + ANY_WALLET_AMOUNT, rechargedBudget.getWallet().getValue());
        assertEquals(ANY_SAVINGS_AMOUNT, rechargedBudget.getSavings().getValue());
        assertEquals(ANY_INSURANCE_POLICY_AMOUNT, rechargedBudget.getInsurancePolicy().getValue());
        assertEquals(ANY_FOOD_EXPENSES_AMOUNT, rechargedBudget.getFoodExpenses().getValue());
    }

    @Test
    public void shouldNotRechargeWalletWhenNoBudget() {
        when(budgetRepository.findAll()).thenReturn(List.of());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> systemUnderTest.recharge(ANY_WALLET_AMOUNT));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("No budget defined", exception.getReason());
    }

    @Test
    public void shouldGetBalance() {
        List<Budget> budget = prepareBudget();
        when(budgetRepository.findAll()).thenReturn(budget);

        BudgetDTO result = systemUnderTest.getBalance();

        assertEquals(ANY_WALLET_AMOUNT, result.getWalletAmount());
        assertEquals(ANY_SAVINGS_AMOUNT, result.getSavingsAmount());
        assertEquals(ANY_INSURANCE_POLICY_AMOUNT, result.getInsurancePolicyAmount());
        assertEquals(ANY_FOOD_EXPENSES_AMOUNT, result.getFoodExpensesAmount());
    }

    @Test
    public void shouldGetBalanceWhenNoBudget() {
        when(budgetRepository.findAll()).thenReturn(List.of());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> systemUnderTest.getBalance());

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("No budget defined", exception.getReason());
    }

    @Test
    public void shouldTransferWhenExactlyAllFunds() {
        List<Budget> budget = prepareBudget();
        when(budgetRepository.findAll()).thenReturn(budget);

        systemUnderTest.transfer(100.0, RegisterType.WALLET, RegisterType.SAVINGS);

        verify(registerRepository, times(2)).save(registerCaptor.capture());
        assertEquals(RegisterType.WALLET, registerCaptor.getAllValues().get(0).getType());
        assertEquals(0.0, registerCaptor.getAllValues().get(0).getValue());
        assertEquals(RegisterType.SAVINGS, registerCaptor.getAllValues().get(1).getType());
        assertEquals(300.0, registerCaptor.getAllValues().get(1).getValue());
    }

    @Test
    public void shouldNotTransferWhenMissingFunds() {
        List<Budget> budget = prepareBudget();
        when(budgetRepository.findAll()).thenReturn(budget);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> systemUnderTest.transfer(101.0, RegisterType.WALLET, RegisterType.SAVINGS));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("No enough money to transfer", exception.getReason());
    }

    @Test
    public void shouldTransferWhenEnoughFundsó() {
        List<Budget> budget = prepareBudget();
        when(budgetRepository.findAll()).thenReturn(budget);

        systemUnderTest.transfer(50.0, RegisterType.WALLET, RegisterType.SAVINGS);

        verify(registerRepository, times(2)).save(registerCaptor.capture());
        assertEquals(RegisterType.WALLET, registerCaptor.getAllValues().get(0).getType());
        assertEquals(50.0, registerCaptor.getAllValues().get(0).getValue());
        assertEquals(RegisterType.SAVINGS, registerCaptor.getAllValues().get(1).getType());
        assertEquals(250.0, registerCaptor.getAllValues().get(1).getValue());
    }

    private List<Budget> prepareBudget() {
        Register wallet = new Register(RegisterType.WALLET, ANY_WALLET_AMOUNT);
        Register savings = new Register(RegisterType.SAVINGS, ANY_SAVINGS_AMOUNT);
        Register insurance = new Register(RegisterType.INSURANCE_POLICY, ANY_INSURANCE_POLICY_AMOUNT);
        Register food = new Register(RegisterType.FOOD_EXPENSES, ANY_FOOD_EXPENSES_AMOUNT);
        Budget budget = new Budget(wallet, savings, insurance, food);
        return List.of(budget);
    }
}
