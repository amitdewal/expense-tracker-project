package com.start.expense_tracker.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.start.expense_tracker.dto.AdminRegisterRequest;
import com.start.expense_tracker.dto.ApiResponse;
import com.start.expense_tracker.dto.UserWithExpensesResponse;
import com.start.expense_tracker.service.config.AdminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
//
//    private final AdminService adminService;
//
//    // ===== Register Admin =====
//    @PostMapping("/register")
//    public ResponseEntity<ApiResponse<Void>> registerAdmin(
//            @RequestBody AdminRegisterRequest request) {
//
//        adminService.registerAdmin(request);
//
//        ApiResponse<Void> response = ApiResponse.<Void>builder()
//                .success(true)
//                .httpStatus(HttpStatus.CREATED.value())
//                .message("Admin registered successfully!")
//                .data(null)
//                .timestamp(LocalDateTime.now())
//                .build();
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//
//    // ===== Get All Users With Expenses =====
//    @GetMapping("/users")
//    @PreAuthorize("hasRole('ADMIN')")   // ← only admin can access
//    public ResponseEntity<ApiResponse<List<UserWithExpensesResponse>>>
//            getAllUsers() {
//
//        List<UserWithExpensesResponse> users =
//                adminService.getAllUsersWithExpenses();
//
//        ApiResponse<List<UserWithExpensesResponse>> response =
//                ApiResponse.<List<UserWithExpensesResponse>>builder()
//                        .success(true)
//                        .httpStatus(HttpStatus.OK.value())
//                        .message("Users fetched successfully!")
//                        .data(users)
//                        .timestamp(LocalDateTime.now())
//                        .build();
//
//        return ResponseEntity.ok(response);
//    }
//
//    // ===== Delete User =====
//    @DeleteMapping("/users/{id}")
//    @PreAuthorize("hasRole('ADMIN')")   // ← only admin can access
//    public ResponseEntity<ApiResponse<Void>> deleteUser(
//            @PathVariable Long id) {
//
//        adminService.deleteUser(id);
//
//        ApiResponse<Void> response = ApiResponse.<Void>builder()
//                .success(true)
//                .httpStatus(HttpStatus.OK.value())
//                .message("User deleted successfully!")
//                .data(null)
//                .timestamp(LocalDateTime.now())
//                .build();
//
//        return ResponseEntity.ok(response);
//    }
}
