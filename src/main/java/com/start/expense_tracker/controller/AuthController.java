package com.start.expense_tracker.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.start.expense_tracker.dto.ApiResponse;
import com.start.expense_tracker.dto.AuthResponse;
import com.start.expense_tracker.dto.LoginRequest;
import com.start.expense_tracker.dto.RefreshTokenRequest;
import com.start.expense_tracker.dto.RegisterRequest;
import com.start.expense_tracker.service.config.AuthService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final AuthService authService;
	
	 // ===== REGISTER =====
	
	@PostMapping("/register")
	public ResponseEntity<ApiResponse<Void>> register(@RequestBody RegisterRequest request){
		 authService.register(request);
		
		ApiResponse<Void> response = ApiResponse.<Void>builder().success(true)
				                    .httpStatus(HttpStatus.CREATED.value())  
									.message("User Register Successfully")
									.data(null)
									.timestamp(LocalDateTime.now())
									.build();
		 return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	 // ===== LOGIN =====
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request){
		
		AuthResponse authResponse = authService.login(request);
		
		ApiResponse<AuthResponse> response = ApiResponse.<AuthResponse>builder().success(true)
				                           .httpStatus(HttpStatus.OK.value())  
				                           .message("Login successful!")
		                                   .data(authResponse)
		                                   .timestamp(LocalDateTime.now())
										   .build();
 		return ResponseEntity.status(HttpStatus.OK).body(response);
		
	}
	
	 // ===== REFRESH TOKEN =====
	@PostMapping("/refresh")
	public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody RefreshTokenRequest request){
  
		AuthResponse authResponse = authService.refreshToken(request);
		 ApiResponse<AuthResponse> apiResponse = ApiResponse.<AuthResponse>builder().success(true)
				                                                                     .httpStatus(HttpStatus.OK.value())  
				                                                                    .message("Token refreshed successfully!")
																			       .data(authResponse)
																			       .timestamp(LocalDateTime.now())
																				   .build();
		 return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
}
	
	
// ===== LOGOUT =====
@PostMapping("/logout")
public ResponseEntity<ApiResponse<Void>> logout(@RequestBody RefreshTokenRequest request) {

	authService.logout(request);

	ApiResponse<Void> response = ApiResponse.<Void>builder().success(true)
			                                                  .httpStatus(HttpStatus.OK.value())  
			                                                 .message("Logged out successfully!")
			                                                .data(null)
			                                                .timestamp(LocalDateTime.now())
			                                                .build();

	return ResponseEntity.status(HttpStatus.OK).body(response);
}
	
}
