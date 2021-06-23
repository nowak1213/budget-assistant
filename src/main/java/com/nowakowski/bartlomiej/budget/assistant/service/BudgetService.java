package com.nowakowski.bartlomiej.budget.assistant.service;

import com.nowakowski.bartlomiej.budget.assistant.BudgetDTO;
import com.nowakowski.bartlomiej.budget.assistant.entity.Budget;
import com.nowakowski.bartlomiej.budget.assistant.entity.Register;
import com.nowakowski.bartlomiej.budget.assistant.entity.RegisterType;
import com.nowakowski.bartlomiej.budget.assistant.repository.BudgetRepository;
import com.nowakowski.bartlomiej.budget.assistant.repository.RegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;

    private final RegisterRepository registerRepository;

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

    public void transfer(Double amount, RegisterType from, RegisterType to) {
        tryChargeRegister(amount, from);
        tryAddAmountToRegister(amount, to);
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

    private void tryChargeRegister(Double amount, RegisterType from) {
        Register budgetRegisterCharged = getChargedRegister(amount, from);
        updateRegister(amount, budgetRegisterCharged);
    }

    private Register getChargedRegister(Double amount, RegisterType from) {
        List<Register> budgetRegisters = getListOfRegisters();
        return budgetRegisters.stream()
                .filter(reg -> reg.getType() == from)
                .filter(reg -> reg.getValue() >= amount)
                .findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "No enough money to transfer"));
    }

    private void updateRegister(Double amount, Register budgetRegisterCharged) {
        budgetRegisterCharged.setValue(budgetRegisterCharged.getValue() - amount);
        registerRepository.save(budgetRegisterCharged);
    }

    private void tryAddAmountToRegister(Double amount, RegisterType to) {
        List<Register> budgetRegisters = getListOfRegisters();
        Optional<Register> budgetRegisterFill = budgetRegisters.stream()
                .filter(reg -> reg.getType() == to)
                .findAny();

        budgetRegisterFill.ifPresent(reg -> {
            reg.setValue(reg.getValue() + amount);
            registerRepository.save(reg);
        });
    }

    private List<Register> getListOfRegisters() {
        Budget budget = getBudget();
        return List.of(budget.getWallet(), budget.getSavings(),
                budget.getInsurancePolicy(), budget.getFoodExpenses());
    }
}
