package com.start.expense_tracker.service.config;

import com.start.expense_tracker.dto.AdminRegisterRequest;
import com.start.expense_tracker.dto.AuthResponse;
import com.start.expense_tracker.dto.UserWithExpensesResponse;
import java.util.List;

public interface AdminService {
	AuthResponse registerAdmin(AdminRegisterRequest request);

	List<UserWithExpensesResponse> getAllUsersWithExpenses();

	void deleteUser(Long userId);
}
