package com.clubnautico.clubnautico.controller;

import com.clubnautico.clubnautico.Service.UserService;
import com.clubnautico.clubnautico.controller.Models.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class UserController {

    private final UserService userService;

    @GetMapping("/give")
    public ResponseEntity<UserResponse> getAuthenticatedUser(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getAuthenticatedUser());
    }
    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateAuthenticatedUser(@RequestBody UserResponse updatedUser) {
        return ResponseEntity.ok(userService.updateAuthenticatedUser(updatedUser));
    }
}
