//package com.start.expense_tracker.security.entity;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//
//@Entity
//@Table(name = "CUSTOM_USER")
//public class CustomUser  implements UserDetails  {
//	
//	 /**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//	 @Id
//	    @GeneratedValue(strategy = GenerationType.IDENTITY)
//	    private Long id;
//
//	 @Column(unique=true)ww
//	    private String username;
//
//	    private String password;
//
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		
//		return new ArrayList<>();
//	}
//
//	@Override
//	public String getPassword() {
//		return password;
//	}
//
//	@Override
//	public String getUsername() {
//		return username;
//	}
//
//}
