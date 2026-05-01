package com.start.expense_tracker.security.JWT;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	
	private final JwtUtil jwtUtil;
	
	private final  UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// ===== Step 1: Get Authorization header =====
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;
		
		
		// ===== Step 2: Check if header has Bearer token =====
		if (authHeader !=null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7); // remove "Bearer part prefix" 
			
			try {
				username = jwtUtil.extractUsername(token);
			} catch (Exception e) {
                // Invalid token — let request continue unauthenticated
				System.out.println("Invalid JWT token: " + e.getMessage());
				
			}
			
		}
		
		 // ===== Step 3: Validate token & set authentication =====
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			if (jwtUtil.validateToken(token, userDetails)) {
				
				 // ===== Step 4: Create authentication object =====
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null,userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));	
				
				 // ===== Step 5: Set authentication in SecurityContext =====
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
			
				
		}
		// ===== Step 6: Continue the filter chain =====
		filterChain.doFilter(request, response);

		
	}

	
}
