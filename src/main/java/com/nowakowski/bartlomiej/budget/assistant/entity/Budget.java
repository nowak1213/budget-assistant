package com.nowakowski.bartlomiej.budget.assistant.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;

import java.util.Objects;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Budget {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @ManyToOne(cascade= CascadeType.PERSIST)
    private Register wallet;

    @ManyToOne(cascade=CascadeType.PERSIST)
    private Register savings;

    @ManyToOne(cascade=CascadeType.PERSIST)
    private Register insurancePolicy;

    @ManyToOne(cascade=CascadeType.PERSIST)
    private Register foodExpenses;

    public Budget(Register wallet, Register savings, Register insurancePolicy, Register foodExpenses) {
        this.wallet = wallet;
        this.savings = savings;
        this.insurancePolicy = insurancePolicy;
        this.foodExpenses = foodExpenses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Budget budget = (Budget) o;
        return Objects.equals(id, budget.id) && Objects.equals(wallet, budget.wallet) && Objects.equals(savings, budget.savings) && Objects.equals(insurancePolicy, budget.insurancePolicy) && Objects.equals(foodExpenses, budget.foodExpenses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, wallet, savings, insurancePolicy, foodExpenses);
    }
}
