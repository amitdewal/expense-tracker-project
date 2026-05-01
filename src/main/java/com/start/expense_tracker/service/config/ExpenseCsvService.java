package com.start.expense_tracker.service.config;

import jakarta.servlet.http.HttpServletResponse;

public interface ExpenseCsvService {
	
	void exportExpensesToCsv(HttpServletResponse  response);

}
