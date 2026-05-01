package com.start.expense_tracker.security.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.start.expense_tracker.entity.User;
import com.start.expense_tracker.security.repository.RefreshTokenRepository;
import com.start.expense_tracker.security.repository.UserRepository;
import com.start.expense_tracker.security.service.entity.RefreshToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {
	
	
	
	@Value("${jwt.refresh-token-expiration}")
	private Long refreshExpiration;

	
	private final RefreshTokenRepository refreshTokenRepository;
	
	private final UserRepository userRepository;
	
	// ===== Create Refresh Token =====
	 @Transactional
	public RefreshToken createRefreshToken(String username) {
		
		// Step 1: Find the user
		User user = userRepository.findByUsername(username)
				                  .orElseThrow(() -> new RuntimeException("User not found: "+username));
		
		// Step 2: Delete old refresh token if exists
		refreshTokenRepository.deleteByUser(user);
		refreshTokenRepository.flush();
		
		// Step 3: Create new refresh token
		RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpiration));
        // Step 4: Save and return
        
        return refreshTokenRepository.save(refreshToken);
		
		
	}
// verify refresh token
	public RefreshToken  verifyExpiration(RefreshToken refreshToken) {
		if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
			refreshTokenRepository.delete(refreshToken);	
			 throw new RuntimeException(
			            "Refresh token expired. Please login again.");
			
		}
		return refreshToken;
		
	}
	 // ===== Delete By User (Logout) =====
	@Transactional
	public void deleteByUser(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found: " + username));
		refreshTokenRepository.deleteByUser(user);
		 log.info("Refresh token deleted for user: {}", username);
	}
	
}
