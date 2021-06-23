package com.nowakowski.bartlomiej.budget.assistant.repository;

import com.nowakowski.bartlomiej.budget.assistant.entity.Budget;
import org.springframework.data.repository.CrudRepository;

public interface BudgetRepository extends CrudRepository<Budget, Long> {
}
