package com.start.expense_tracker.service.config;

import com.start.expense_tracker.dto.AuthResponse;
import com.start.expense_tracker.dto.LoginRequest;
import com.start.expense_tracker.dto.RefreshTokenRequest;
import com.start.expense_tracker.dto.RegisterRequest;

public interface AuthService {

	// Register new user

	AuthResponse register(RegisterRequest request);
	
	// login user -> return token
	
	AuthResponse login(LoginRequest request);
	
	// Refresh access token
	
	AuthResponse refreshToken(RefreshTokenRequest request);
	
	//logout user
	void logout(RefreshTokenRequest request);

}
