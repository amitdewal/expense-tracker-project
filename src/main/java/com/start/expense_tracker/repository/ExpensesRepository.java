package com.start.expense_tracker.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.start.expense_tracker.entity.Category;
import com.start.expense_tracker.entity.Expense;
import com.start.expense_tracker.entity.User;

@Repository
public interface ExpensesRepository extends JpaRepository<Expense, Long> {

	List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);

	List<Expense> findByCategory(Category category);

	// Find expenses whose id is in the provided list (uses SQL IN under the hood)
	List<Expense> findByIdIn(List<Long> ids);

	// Find expenses whose category is in the provided list (uses SQL IN under the
	// hood)
	List<Expense> findByCategoryIn(List<Category> categories);

	// ✅ Find all expenses by user
	List<Expense> findByUser(User user);

	// ✅ Find by date range for specific user
	List<Expense> findByUserAndDateBetween(User user, LocalDate start, LocalDate end);

	// ✅ Find by category for specific user
	List<Expense> findByUserAndCategory(User user, Category category);

	// ✅ Find by ids for specific user
	List<Expense> findByUserAndIdIn(User user, List<Long> ids);

}