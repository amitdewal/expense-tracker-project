package com.start.expense_tracker.mapper;

import com.start.expense_tracker.dto.ExpenseRequest;
import com.start.expense_tracker.dto.ExpenseResponse;
import com.start.expense_tracker.entity.Expense;

public class ExpenseMapper {
 private ExpenseMapper() {
  
 }

	


	public static Expense toEntity(ExpenseRequest request) {
        Expense expense = new Expense();
        expense.setDate(request.getDate());
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory()); // ✅ added
        return expense;
    }

    public static ExpenseResponse toResponse(Expense expense) {
        ExpenseResponse response = new ExpenseResponse();
        response.setId(expense.getId());
        response.setDate(expense.getDate());
        response.setDescription(expense.getDescription());
        response.setAmount(expense.getAmount());
        response.setCategory(expense.getCategory()); // ✅ added
        response.setCreatedAt(expense.getCreatedAt());
        response.setUpdatedAt(expense.getUpdatedAt());
        return response;
    }
}
