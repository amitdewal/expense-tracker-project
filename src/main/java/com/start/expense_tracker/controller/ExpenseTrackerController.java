package com.start.expense_tracker.controller;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.start.expense_tracker.constants.ApiPaths;
import com.start.expense_tracker.constants.ExpenseMessages;
import com.start.expense_tracker.dto.ApiResponse;
import com.start.expense_tracker.dto.ExpenseRequest;
import com.start.expense_tracker.dto.ExpenseResponse;
import com.start.expense_tracker.dto.ExpenseSummaryResponse;
import com.start.expense_tracker.entity.Category;
import com.start.expense_tracker.service.ExpenseService;
import com.start.expense_tracker.service.config.ExpenseCsvService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(ApiPaths.EXPENSE)
public class ExpenseTrackerController {

	private final ExpenseService expenseService;
	private final ExpenseCsvService expenseCsvService;


	Logger logger = LoggerFactory.getLogger(ExpenseTrackerController.class);

	@PostMapping
	public ResponseEntity<ApiResponse<ExpenseResponse>> addExpenses(@Valid @RequestBody ExpenseRequest request) {
		ExpenseResponse expenseResponse = expenseService.createExpense(request);
		ApiResponse<ExpenseResponse> apiResponse = ApiResponse.<ExpenseResponse>builder()
				.success(true)
				.httpStatus(HttpStatus.CREATED.value())  
				.message(ExpenseMessages.CREATED)
				.data(expenseResponse)
				.timestamp(LocalDateTime.now())
				.build();

		log.info("Successfully created expense with description: {}", expenseResponse.getDescription());

		return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);

	}

	@GetMapping()
	public ResponseEntity<ApiResponse<List<ExpenseResponse>>> getAllExpenses() {

		List<ExpenseResponse> allExpenses = expenseService.getAllExpenses();
		ApiResponse<List<ExpenseResponse>> apiResponse = ApiResponse.<List<ExpenseResponse>>builder()
				.success(true)
				.httpStatus(HttpStatus.OK.value())  
				.message(ExpenseMessages.FETCHED)
				.data(allExpenses)
				.timestamp(LocalDateTime.now())
				.build();
		log.info("Successfully fetched {} expenses", allExpenses.size());
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<ExpenseResponse>> updateExpense(@PathVariable Long id,
			@Valid @RequestBody ExpenseRequest request) {
		ExpenseResponse updatedExpense = expenseService.updateExpense(id, request);
		ApiResponse<ExpenseResponse> apiResponse = ApiResponse.<ExpenseResponse>builder()
				.success(true)
				.httpStatus(HttpStatus.OK.value())
				.message(ExpenseMessages.UPDATED)
				.data(updatedExpense)
				.timestamp(LocalDateTime.now()).build();
		log.info("Successfully updated expense with id: {}", id);
		return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteExpense(@PathVariable Long id) {
	    expenseService.deleteExpense(id);
	    ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
	            .success(true)
	            .httpStatus(HttpStatus.OK.value())
	            .message(ExpenseMessages.DELETED)
	            .data(null)
	            .timestamp(LocalDateTime.now())
	            .build();

	    log.info("Successfully deleted expense with id: {}", id);

	    return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}
	
	
	@GetMapping("/summary")
	public ResponseEntity<ApiResponse<ExpenseSummaryResponse>> getTotalSummary() {

	    ExpenseSummaryResponse summary = expenseService.getTotalSummary();

	    ApiResponse<ExpenseSummaryResponse> apiResponse =
	            ApiResponse.<ExpenseSummaryResponse>builder()
	                    .success(true)
	                    .httpStatus(HttpStatus.OK.value())  
	                    .message(ExpenseMessages.TOTAL_SUMMARY_FETCHED)
	                    .data(summary)
	                    .timestamp(LocalDateTime.now())
	                    .build();

	    return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}
	
	//Users can view a summary of expenses for a specific month (of current year).
	@GetMapping("/summary/{yearMonth}")
	public ResponseEntity<ApiResponse<ExpenseSummaryResponse>> getMonthlySummary(@PathVariable String yearMonth) {
	    ExpenseSummaryResponse summary = expenseService.getMonthlySummary(YearMonth.parse(yearMonth));

	    ApiResponse<ExpenseSummaryResponse> apiResponse =
	            ApiResponse.<ExpenseSummaryResponse>builder()
	                    .success(true)
	                    .httpStatus(HttpStatus.OK.value())  
	                    .message(ExpenseMessages.MONTHLY_SUMMARY_FETCHED)
	                    .data(summary)
	                    .timestamp(LocalDateTime.now())
	                    .build();

	    return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}
	
	@GetMapping("/category/{category}")
	public ResponseEntity<ApiResponse<List<ExpenseResponse>>> getByCategory(
	        @PathVariable Category category) {

	    List<ExpenseResponse> expenses = expenseService.getByCategory(category);

	    ApiResponse<List<ExpenseResponse>> apiResponse =
	            ApiResponse.<List<ExpenseResponse>>builder()
	                    .success(true)
	                    .httpStatus(HttpStatus.OK.value())  
	                    .message(ExpenseMessages.EXPENSES_BY_CATEGORY_FETCHED)
	                    .data(expenses)
	                    .timestamp(LocalDateTime.now())
	                    .build();

	    return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
	}

	// get expenses by id
	@GetMapping("/ids")
	public ResponseEntity<ApiResponse<List<ExpenseResponse>>> getByIds(@RequestParam(required = false) List<Long> ids) {
	    List<ExpenseResponse> expenses = expenseService.getByIds(ids);

	    ApiResponse<List<ExpenseResponse>> apiResponse =
	            ApiResponse.<List<ExpenseResponse>>builder()
	                    .success(true)
	                    .httpStatus(HttpStatus.OK.value())  
	                    .message(ExpenseMessages.EXPENSES_BY_IDS_FETCHED)
	                    .data(expenses)
	                    .timestamp(LocalDateTime.now())
	                    .build();

	    return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

}
	// ===== CSV Export =====
	
	@GetMapping("/export")
	public void exportToCsv(HttpServletResponse response) {
	    expenseCsvService.exportExpensesToCsv(response);
	    log.info("CSV export triggered");
	}
}