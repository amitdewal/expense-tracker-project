package com.start.expense_tracker.service;

import java.time.YearMonth;
import java.util.List;

import com.start.expense_tracker.dto.ExpenseRequest;
import com.start.expense_tracker.dto.ExpenseResponse;
import com.start.expense_tracker.dto.ExpenseSummaryResponse;
import com.start.expense_tracker.entity.Category;

public interface ExpenseService {

//	 add an expense 
	ExpenseResponse createExpense(ExpenseRequest request);

//	 update an expense.
	ExpenseResponse updateExpense(Long id,ExpenseRequest request);

//	 delete an expense.
	void deleteExpense(Long id);

//	 view all expenses.
	List<ExpenseResponse> getAllExpenses();

//	 view summary of expenses
	ExpenseSummaryResponse  getTotalSummary();

// view a summary of expenses for a specific month (of current year)
	ExpenseSummaryResponse getMonthlySummary(YearMonth yearMonth);
	
	public List<ExpenseResponse> getByCategory(Category category);

	// Find expenses by a list of ids
	List<ExpenseResponse> getByIds(List<Long> ids);

	// Find expenses by a list of categories
//	List<ExpenseResponse> getByCategories(List<Long> ids);

}