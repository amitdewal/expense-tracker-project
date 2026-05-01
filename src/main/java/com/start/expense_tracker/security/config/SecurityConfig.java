package com.start.expense_tracker.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.start.expense_tracker.security.JWT.JwtAuthFilter;
import com.start.expense_tracker.security.user.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
	
	
	private final JwtAuthFilter jwtAuthFilter;
	
	private final UserDetailsServiceImpl userDetailsService;
	
	private final AuthEntryPoint authEntryPoint; 
	
	// ===== Security Filter Chain =====
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		 // Step 1: Disable CSRF (not needed for REST APIs)
		http.csrf(csrf -> csrf.disable())
		
		// Step 2: Set session to STATELESS (no sessions, only JWT)
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		
		// Step 3: Define public and protected endpoints
		
		.authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**").permitAll() //public
				.anyRequest().authenticated())                                         // // all others need JWT
		  .exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint))
		
		// Step 4: Set authentication provider
		.authenticationProvider(authenticationProvider())
		
		 // Step 5: Add JWT filter before Spring's default auth filter
		.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
		
	}


	// ===== Authentication Provider =====
	@SuppressWarnings("deprecation")
	@Bean
	public  DaoAuthenticationProvider  authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
	}


	 // ===== Password Encoder =====
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// ===== Authentication Manager =====
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
	

}
