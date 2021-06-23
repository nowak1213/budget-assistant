package com.nowakowski.bartlomiej.budget.assistant.controller;

import com.nowakowski.bartlomiej.budget.assistant.service.BudgetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BudgetController.class)
public class BudgetControllerMVCTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    public BudgetService budgetService;

    @Test
    void shouldRechargeWalletAndGet200WhenCorrectAmount() throws Exception {
        Double anyCorrectAmount = 10.0;

        mockMvc.perform(post("/budget/assistant/recharge/" + anyCorrectAmount)
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotRechargeWalletAndGet400WhenNegativeAmount() throws Exception {
        double anyIncorrectAmount = -10.0;

        mockMvc.perform(post("/budget/assistant/recharge/" + anyIncorrectAmount)
                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotRechargeWalletAndGet404WhenNoBudget() throws Exception {
        double anyCorrectAmount = 10.0;
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "No budget defined"))
                .when(budgetService).recharge(anyCorrectAmount);

        mockMvc.perform(post("/budget/assistant/recharge/" + anyCorrectAmount)
                .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetBalanceAndGet200() throws Exception {
        mockMvc.perform(get("/budget/assistant")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetBalanceAndGet404WhenNoBudget() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "No budget defined"))
                .when(budgetService).getBalance();

        mockMvc.perform(get("/budget/assistant")
                .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldTransferAndGet200WhenCorrectAmount() throws Exception {
        double anyCorrectAmount = 10.0;

        mockMvc.perform(post("/budget/assistant/transfer/" + anyCorrectAmount + "?from=WALLET&to=SAVINGS")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotTransferAndGet400WhenNegativeAmount() throws Exception {
        double anyIncorrectAmount = -10.0;

        mockMvc.perform(post("/budget/assistant/transfer/" + anyIncorrectAmount + "?from=WALLET&to=SAVINGS")
                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotTransferAndGet404WhenNotEnoughFunds() throws Exception {
        double anyIncorrectAmount = 10.0;
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "No enough money to transfer"))
                .when(budgetService).transfer(any(), any(), any());

        mockMvc.perform(post("/budget/assistant/transfer/" + anyIncorrectAmount + "?from=WALLET&to=SAVINGS")
                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }
}
