package com.start.expense_tracker.security.utils;

import com.start.expense_tracker.entity.User;
import com.start.expense_tracker.security.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

	private final UserRepository userRepository;

	// ===== Get currently logged in user =====
	public User getCurrentUser() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		return userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found: " + username));
	}
}
