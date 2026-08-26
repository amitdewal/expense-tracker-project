package com.start.expense_tracker.service.impl;

import com.start.expense_tracker.dto.AdminRegisterRequest;
import com.start.expense_tracker.dto.AuthResponse;
import com.start.expense_tracker.dto.ExpenseResponse;
import com.start.expense_tracker.dto.UserWithExpensesResponse;
import com.start.expense_tracker.entity.User;
import com.start.expense_tracker.mapper.ExpenseMapper;
import com.start.expense_tracker.repository.ExpensesRepository;
import com.start.expense_tracker.security.JWT.JwtUtil;
import com.start.expense_tracker.security.repository.UserRepository;
import com.start.expense_tracker.security.service.RefreshTokenService;
import com.start.expense_tracker.service.config.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {@Override
	public AuthResponse registerAdmin(AdminRegisterRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserWithExpensesResponse> getAllUsersWithExpenses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUser(Long userId) {
		// TODO Auto-generated method stub
		
	}

//	private final UserRepository userRepository;
//	private final ExpensesRepository expensesRepository;
//	private final PasswordEncoder passwordEncoder;
//	private final JwtUtil jwtUtil;
//	private final RefreshTokenService refreshTokenService;
//
//	@Value("${app.admin.secret-key}")
//	private String adminSecretKey;
//
//	// ===== Register Admin =====
//	@Override
//	public AuthResponse registerAdmin(AdminRegisterRequest request) {
//
//		// Step 1: Validate secret key
//		if (!adminSecretKey.equals(request.getAdminSecretKey())) {
//			throw new RuntimeException("Invalid admin secret key!");
//		}
//
//		// Step 2: Check duplicate username
//		if (userRepository.existsByUsername(request.getUsername())) {
//			throw new RuntimeException("Username already exists!");
//		}
//
//		// Step 3: Check duplicate email
//		if (userRepository.existsByEmail(request.getEmail())) {
//			throw new RuntimeException("Email already exists!");
//		}
//
//		// Step 4: Save admin user
//		User admin = new User();
//		admin.setUsername(request.getUsername());
//		admin.setEmail(request.getEmail());
//		admin.setPassword(passwordEncoder.encode(request.getPassword()));
//		admin.setRole("ROLE_ADMIN"); // ← admin role
//		userRepository.save(admin);
//
//		log.info("New admin registered: {}", request.getUsername());
//		return null;
//	}
//
//	// ===== Get All Users With Expenses =====
//	@Override
//	public List<UserWithExpensesResponse> getAllUsersWithExpenses() {
//		List<User> users = userRepository.findAll();
//
//		return users.stream().map(user -> {
//			List<ExpenseResponse> expenses = expensesRepository.findAll().stream().map(ExpenseMapper::toResponse)
//					.toList();
//
//			return UserWithExpensesResponse.builder().id(user.getId()).username(user.getUsername())
//					.email(user.getEmail()).role(user.getRole()).totalExpenses(expenses.size()).expenses(expenses)
//					.build();
//		}).toList();
//	}
//
//	// ===== Delete User =====
//	@Override
//	@Transactional
//	public void deleteUser(Long userId) {
//		User user = userRepository.findById(userId)
//				.orElseThrow(() -> new RuntimeException("User not found: " + userId));
//
//		// Delete user's expenses first
//		expensesRepository.deleteAll(expensesRepository.findAll().stream().filter(e -> true).toList());
//
//		userRepository.delete(user);
//		log.info("User deleted by admin: {}", userId);
//	}
}