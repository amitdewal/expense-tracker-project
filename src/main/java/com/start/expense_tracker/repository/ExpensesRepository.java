package com.start.expense_tracker.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.start.expense_tracker.entity.Category;
import com.start.expense_tracker.entity.Expense;


@Repository
public interface ExpensesRepository extends JpaRepository<Expense, Long> {

	 List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);

	 List<Expense> findByCategory(Category category);

	 // Find expenses whose id is in the provided list (uses SQL IN under the hood)
	 List<Expense> findByIdIn(List<Long> ids);

	 // Find expenses whose category is in the provided list (uses SQL IN under the hood)
	 List<Expense> findByCategoryIn(List<Category> categories);

}