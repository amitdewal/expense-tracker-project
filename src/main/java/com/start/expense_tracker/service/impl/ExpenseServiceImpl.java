package com.start.expense_tracker.service.impl;

import java.time.YearMonth;
import java.util.List;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.start.expense_tracker.controller.advice.ResourceNotFoundException;
import com.start.expense_tracker.dto.ExpenseRequest;
import com.start.expense_tracker.dto.ExpenseResponse;
import com.start.expense_tracker.dto.ExpenseSummaryResponse;
import com.start.expense_tracker.entity.Category;
import com.start.expense_tracker.entity.Expense;
import com.start.expense_tracker.mapper.ExpenseMapper;
import com.start.expense_tracker.repository.ExpensesRepository;
import com.start.expense_tracker.service.ExpenseService;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class ExpenseServiceImpl implements ExpenseService {
	
	private final ExpensesRepository expenseRepository;
	



	@Override
	public ExpenseResponse createExpense(ExpenseRequest request) {
			Expense expense = ExpenseMapper.toEntity(request);
			Expense saved = expenseRepository.save(expense);
			return ExpenseMapper.toResponse(saved);
		
	}

	@Override  
	public ExpenseResponse updateExpense(Long id,ExpenseRequest request) {
		Expense expense = expenseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
		expense.setDescription(request.getDescription());
		expense.setAmount(request.getAmount());
		expense.setDate(request.getDate());
		Expense updated = expenseRepository.save(expense);
		return ExpenseMapper.toResponse(updated);
	}

	@Override
	public void deleteExpense(Long id) {

	    Expense expense = expenseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));

	    expenseRepository.delete(expense);
	}

	@Override
	public List<ExpenseResponse> getAllExpenses() {
		 List<Expense> expenses = expenseRepository.findAll();
		 return expenses.stream().map(ExpenseMapper::toResponse).toList();
	}

	@Override
	public ExpenseSummaryResponse getTotalSummary() {
		List<Expense> expenses = expenseRepository.findAll();
		BigDecimal totalAmount = expenses.stream()
				.map(Expense::getAmount)
				.filter(a -> a != null)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		long totalCount = expenses.size();
		return ExpenseSummaryResponse.builder()
				.totalAmount(totalAmount)
				.totalCount(totalCount)
				.build();
	}

	@Override
	public ExpenseSummaryResponse getMonthlySummary(YearMonth yearMonth) {
		List<Expense> byDateBetween = expenseRepository.findByDateBetween(yearMonth.atDay(1), yearMonth.atEndOfMonth());
		BigDecimal totalAmount = byDateBetween.stream()
				.map(Expense::getAmount)
				.filter(a -> a != null)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		long totalCount = byDateBetween.size();
		return ExpenseSummaryResponse.builder()
				.totalAmount(totalAmount)
				.totalCount(totalCount)
				.build();	
	}

	@Override
	public List<ExpenseResponse> getByCategory(Category category) {
		List<Expense> byCategory = expenseRepository.findByCategory(category);
		return byCategory.stream().map(ExpenseMapper::toResponse).toList();
	}

	@Override
	public List<ExpenseResponse> getByIds(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			throw new ResourceNotFoundException("No ids provided");
		}
		List<Expense> byIds = expenseRepository.findByIdIn(ids);
		if(byIds.isEmpty()) {
			throw new ResourceNotFoundException("No expenses found with the provided ids");
		}
		return byIds.stream().map(ExpenseMapper::toResponse).toList();
	}



}
