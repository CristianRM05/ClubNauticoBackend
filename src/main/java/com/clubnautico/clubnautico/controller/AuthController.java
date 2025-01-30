package com.clubnautico.clubnautico.controller;

import com.clubnautico.clubnautico.Config.JwtService;
import com.clubnautico.clubnautico.Service.AauthService;
import com.clubnautico.clubnautico.controller.Models.AuthResponse;
import com.clubnautico.clubnautico.controller.Models.AuthenticateRequest;
import com.clubnautico.clubnautico.controller.Models.RegisterRequest;
import com.clubnautico.clubnautico.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class AuthController {
    @Autowired
    private AauthService authService;
    @Autowired
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthenticateRequest request){
        return ResponseEntity.ok(authService.authenticate(request));
    }
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        try {
            boolean isValid = !jwtService.isTokenExpired(token);
            return ResponseEntity.ok(isValid);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }






}
