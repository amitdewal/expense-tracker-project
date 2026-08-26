package com.start.expense_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWithExpensesResponse {
	private Long id;
	private String username;
	private String email;
	private String role;
	private int totalExpenses;
	private List<ExpenseResponse> expenses;
}
