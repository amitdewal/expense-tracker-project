package com.start.expense_tracker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRegisterRequest {
	private String username;
	private String email;
	private String password;
	private String adminSecretKey; // ← must match env variable
}
