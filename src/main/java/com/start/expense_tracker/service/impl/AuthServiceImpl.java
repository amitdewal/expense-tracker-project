package com.start.expense_tracker.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.start.expense_tracker.dto.AuthResponse;
import com.start.expense_tracker.dto.LoginRequest;
import com.start.expense_tracker.dto.RefreshTokenRequest;
import com.start.expense_tracker.dto.RegisterRequest;
import com.start.expense_tracker.entity.User;
import com.start.expense_tracker.security.JWT.JwtUtil;
import com.start.expense_tracker.security.repository.RefreshTokenRepository;
import com.start.expense_tracker.security.repository.UserRepository;
import com.start.expense_tracker.security.service.RefreshTokenService;
import com.start.expense_tracker.security.service.entity.RefreshToken;
import com.start.expense_tracker.service.config.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@RequiredArgsConstructor
@Slf4j
@Service
public class AuthServiceImpl implements AuthService{
	
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

	@Override
	public AuthResponse register(RegisterRequest request) {
		// Step 1: Check duplicate username
		Boolean existsByUsername = userRepository.existsByUsername(request.getUsername());
		if (Boolean.TRUE.equals(existsByUsername)) {
			 throw new RuntimeException("Username already exists!");
		}
		
		// Step 2: Check duplicate email
		Boolean existsByEmail = userRepository.existsByEmail(request.getEmail());
		if (Boolean.TRUE.equals(existsByEmail)) {
			throw new RuntimeException("Email already exists!");
		}
		// Step 3: Build and save new user
		User user = new User();
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole("ROLE_USER");
		userRepository.save(user);
		log.info("New user registered with username: {}", request.getUsername());
		
		 // Step 4: Return null data (no tokens on register)
		
		return null;
	}

	@Override
	public AuthResponse login(LoginRequest request) {
		
		// Step 1: Authenticate username & password
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		
		// Step 2: Load user from DB
		User user	 = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
		
		 // Step 3: Generate access token
		String accessToken = jwtUtil.generateAccessToken(request.getUsername());
		// Step 4: Generate refresh token
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.getUsername());
		
		 log.info("User logged in successfully: {}", request.getUsername());
		 
		// Step 5: Return tokens + user info
		 
		 
		return new  AuthResponse(accessToken,refreshToken.getToken(),user.getUsername(),user.getEmail());
	}

	// ===== REFRESH TOKEN =====
	@Override
	public AuthResponse refreshToken(RefreshTokenRequest request) {
		// Step 1: Find refresh token in DB
		RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken()) .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
		
		// Step 2: Verify not expired
		
		refreshTokenService.verifyExpiration(refreshToken);
		
		 // Step 3: Generate new access token
		String newAccessToken = jwtUtil.generateAccessToken(refreshToken.getUser().getUsername());
		
		 log.info("Access token refreshed for user: {}",
	                refreshToken.getUser().getUsername());
		 
		 // Step 4: Return new access token + same refresh token
		 
		 
		
			return new AuthResponse(newAccessToken, refreshToken.getToken(), refreshToken.getUser().getUsername(),
					refreshToken.getUser().getEmail());
		
	}

	@Override
	public void logout(RefreshTokenRequest request) {
		 // Step 1: Find and delete refresh token
		refreshTokenRepository.findByToken(request.getRefreshToken())
		.ifPresent(refreshTokenRepository::delete);
		log.info("User logged out successfully");
	}

}
