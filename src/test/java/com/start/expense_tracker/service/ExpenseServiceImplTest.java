package com.start.expense_tracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.start.expense_tracker.controller.advice.ResourceNotFoundException;
import com.start.expense_tracker.dto.ExpenseRequest;
import com.start.expense_tracker.dto.ExpenseResponse;
import com.start.expense_tracker.entity.Category;
import com.start.expense_tracker.entity.Expense;
import com.start.expense_tracker.repository.ExpensesRepository;
import com.start.expense_tracker.service.impl.ExpenseServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceImplTest {

	@Mock
	private ExpensesRepository expenseRepository;

	@InjectMocks
	private ExpenseServiceImpl expenseServiceImpl;

	@Test
	void createExpense_shouldReturnResponse() {
		// ARRANGE — set up test data + mock behaviour
		ExpenseRequest request = new ExpenseRequest();
		request.setAmount(new BigDecimal("12.34"));
		request.setCategory(Category.FOOD);
		request.setDescription("Dinner at hotel");
		request.setDate(LocalDate.of(2026, 4, 21));

		Expense expense = new Expense();
		expense.setId(1L);
		expense.setAmount(new BigDecimal("12.34"));
		expense.setCategory(Category.FOOD);
		expense.setDescription("Dinner at hotel");
		expense.setDate(LocalDate.of(2026, 4, 21));

//		ExpenseResponse expenseResponse = new ExpenseResponse();
		// ACT — call the method

		when(expenseRepository.save(any(Expense.class))).thenReturn(expense);
		// ASSERT — verify the result

		ExpenseResponse response = expenseServiceImpl.createExpense(request);

		assertNotNull(response);

		assertEquals("Dinner at hotel", response.getDescription());
		assertEquals(new BigDecimal("12.34"), response.getAmount());
		assertEquals(Category.FOOD, response.getCategory());
		verify(expenseRepository, times(1)).save(any(Expense.class));

	}

	@Test
	void getAllExpenses_shouldReturnListOfResponses() {
		Expense expense = new Expense();
		expense.setId(1L);
		expense.setAmount(new BigDecimal("12.34"));
		expense.setCategory(Category.FOOD);
		expense.setDescription("Dinner at hotel");
		expense.setDate(LocalDate.of(2026, 4, 21));

		List<Expense> list = List.of(expense);

		when(expenseRepository.findAll()).thenReturn(list);

		List<ExpenseResponse> expenses = expenseServiceImpl.getAllExpenses();
		assertNotNull(expenses);

		assertEquals(1, expenses.size());

		verify(expenseRepository, times(1)).findAll();

	}

	@Test
	void getAllExpenses_shouldReturnEmptyList_whenNoExpenses() {
		List<Expense> list = List.of();

		when(expenseRepository.findAll()).thenReturn(list);
		List<ExpenseResponse> expenses = expenseServiceImpl.getAllExpenses();
		assertNotNull(expenses);

		assertEquals(0, expenses.size());
		verify(expenseRepository, times(1)).findAll();

	}

	@Test
	void deleteExpense_shouldDeleteSuccessfully() {
		Expense expense = new Expense();
		expense.setId(1L);
		expense.setAmount(new BigDecimal("12.34"));
		expense.setCategory(Category.FOOD);
		expense.setDescription("Dinner at hotel");
		expense.setDate(LocalDate.of(2026, 4, 21));

		when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));

		expenseServiceImpl.deleteExpense(1L);
		verify(expenseRepository, times(1)).delete(expense);

	}

	@Test
	void deleteExpense_shouldThrowException_whenIdNotFound() {
		when(expenseRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			expenseServiceImpl.deleteExpense(1L);
		});

		verify(expenseRepository, never()).delete(any());

	}

}
