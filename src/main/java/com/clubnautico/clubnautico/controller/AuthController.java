package com.clubnautico.clubnautico.controller;

import com.clubnautico.clubnautico.Service.AauthService;
import com.clubnautico.clubnautico.controller.Models.AuthResponse;
import com.clubnautico.clubnautico.controller.Models.AuthenticateRequest;
import com.clubnautico.clubnautico.controller.Models.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
@Autowired
 private AauthService authService;

@PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthenticateRequest request){
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
