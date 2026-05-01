package com.start.expense_tracker.service.impl;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.start.expense_tracker.entity.Expense;
import com.start.expense_tracker.repository.ExpensesRepository;
import com.start.expense_tracker.service.config.ExpenseCsvService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExpenseCsvServiceImpl implements ExpenseCsvService {
	
	private final ExpensesRepository repository;
	
	// ===== Clean date format =====
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


	@Override
	public void exportExpensesToCsv(HttpServletResponse response) {
		DateTimeFormatter fileFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm");

		String fileName = "expenses-" + LocalDateTime.now().format(fileFormatter) + ".csv";

		
		
		// Step 1: Set response headers
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		
		
		  // Step 2: Fetch all expenses
		
		List<Expense> expenses = repository.findAll();
		
		try {
			// Step 3: Write CSV header row
		PrintWriter writer = response.getWriter();
		writer.println( "ID,Date,Description,Amount,Category,CreatedAt");
		
		// Step 4: Write each expense as a row
		
		for (Expense expense : expenses) {
			
			String createdAt = expense.getCreatedAt() != null
                    ? expense.getCreatedAt().format(FORMATTER)
                    : "";
			writer.println(expense.getId() + "," + 
		                   expense.getDate() + "," + 
					       escapeCsv(expense.getDescription()) + "," + 
		                   expense.getAmount() + "," + 
					       expense.getCategory() + "," + 
					       createdAt);
		}
		writer.flush();
		 log.info("CSV export completed — {} records exported",
                 expenses.size());
		
		
		} catch (Exception e) {
			log.error("CSV export failed: {}", e.getMessage());
			 throw new RuntimeException("Failed to export CSV");
		}
		
		
		
		 
		
		
	}
	// ===== Escape commas and quotes in text fields =====
	private String escapeCsv(String value) {
		 if (value == null) return "";
	        // If value contains comma or quote → wrap in quotes
	        if (value.contains(",") || value.contains("\"")
	                || value.contains("\n")) {
	            value = value.replace("\"", "\"\"");
	            return "\"" + value + "\"";
	        }
	        return value;
	    }
	}


