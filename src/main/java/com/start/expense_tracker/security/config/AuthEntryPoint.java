package com.start.expense_tracker.security.config;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.start.expense_tracker.dto.ApiResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {
	
    private final ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		// Step 1: Set response type to JSON
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //403
		
		  // Step 2: Build your standard ApiResponse
		
		ApiResponse<Void> apiResponse = ApiResponse.<Void>builder().success(false)
		                            .message("Access denied! Please login first.")
		                            .data(null)
		                            .timestamp(LocalDateTime.now())
		                            .build();
		
		 // Step 3: Write JSON to response
		  objectMapper.writeValue(response.getOutputStream(), apiResponse);
		
	}

}
