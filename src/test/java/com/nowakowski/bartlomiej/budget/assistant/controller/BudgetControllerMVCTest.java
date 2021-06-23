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

import static org.mockito.Mockito.doThrow;
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
        double anyCorrectAmount = 10.0;

        mockMvc.perform(post("/budget/assistant/" + anyCorrectAmount)
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotRechargeWalletAndGet400WhenNegativeAmount() throws Exception {
        double anyIncorrectAmount = -10.0;

        mockMvc.perform(post("/budget/assistant/" + anyIncorrectAmount)
                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotRechargeWalletAndGet404WhenNoBudget() throws Exception {
        double anyCorrectAmount = 10.0;
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "No budget defined"))
                .when(budgetService).recharge(anyCorrectAmount);

        mockMvc.perform(post("/budget/assistant/" + anyCorrectAmount)
                .contentType("application/json"))
                .andExpect(status().isNotFound());
    }
}
