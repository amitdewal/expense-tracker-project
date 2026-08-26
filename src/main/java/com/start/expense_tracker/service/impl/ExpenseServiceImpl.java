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
import com.start.expense_tracker.entity.User;
import com.start.expense_tracker.mapper.ExpenseMapper;
import com.start.expense_tracker.repository.ExpensesRepository;
import com.start.expense_tracker.security.utils.SecurityUtils;
import com.start.expense_tracker.service.ExpenseService;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class ExpenseServiceImpl implements ExpenseService {
	
	private final ExpensesRepository expenseRepository;
	private final SecurityUtils securityUtils; 



	@Override
	public ExpenseResponse createExpense(ExpenseRequest request) {
		  User currentUser = securityUtils.getCurrentUser();
			Expense expense = ExpenseMapper.toEntity(request);
			expense.setUser(currentUser);
			Expense saved = expenseRepository.save(expense);
			return ExpenseMapper.toResponse(saved);
		
	}

	@Override  
	public ExpenseResponse updateExpense(Long id,ExpenseRequest request) {
		 User currentUser = securityUtils.getCurrentUser();
		Expense expense = expenseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
		
		if(!expense.getUser().getId().equals(currentUser.getId()) ) {
			throw new RuntimeException(
	                "You are not authorized to update this expense!");
		}
		
		expense.setDescription(request.getDescription());
		expense.setAmount(request.getAmount());
		expense.setDate(request.getDate());
		Expense updated = expenseRepository.save(expense);
		return ExpenseMapper.toResponse(updated);
	}

	@Override
	public void deleteExpense(Long id) {
		User currentUser = securityUtils.getCurrentUser();
	    Expense expense = expenseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
	    if (!expense.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException(
                "You are not authorized to delete this expense!");
        }

	    expenseRepository.delete(expense);
	}

	@Override
	public List<ExpenseResponse> getAllExpenses() {
		User currentUser = securityUtils.getCurrentUser();
		 return expenseRepository.findByUser(currentUser)
	                .stream()
	                .map(ExpenseMapper::toResponse)
	                .toList();
	}

	@Override
	public ExpenseSummaryResponse getTotalSummary() {
		User currentUser = securityUtils.getCurrentUser();
		List<Expense> expenses =
	            expenseRepository.findByUser(currentUser);
		
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
		User currentUser = securityUtils.getCurrentUser();
		
		List<Expense> byDateBetween = expenseRepository.findByUserAndDateBetween(currentUser,yearMonth.atDay(1), yearMonth.atEndOfMonth());
		
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
		User currentUser = securityUtils.getCurrentUser();
		
		return expenseRepository
                .findByUserAndCategory(currentUser, category)
                .stream()
                .map(ExpenseMapper::toResponse)
                .toList();
	}

	@Override
	public List<ExpenseResponse> getByIds(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			throw new ResourceNotFoundException("No ids provided");
		}
		 User currentUser = securityUtils.getCurrentUser();
		 
		 List<Expense> expenses =
		            expenseRepository.findByUserAndIdIn(currentUser, ids);

		 if (expenses.isEmpty()) {
	            throw new ResourceNotFoundException(
	                "No expenses found with the provided ids");
	        }
		 return expenses.stream()
	                .map(ExpenseMapper::toResponse)
	                .toList();
	}



}
