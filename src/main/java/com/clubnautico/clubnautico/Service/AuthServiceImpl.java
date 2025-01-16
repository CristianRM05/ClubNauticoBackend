package com.clubnautico.clubnautico.Service;

import com.clubnautico.clubnautico.Config.JwtService;
import com.clubnautico.clubnautico.controller.Models.AuthResponse;
import com.clubnautico.clubnautico.controller.Models.AuthenticateRequest;
import com.clubnautico.clubnautico.controller.Models.RegisterRequest;
import com.clubnautico.clubnautico.entity.User;
import com.clubnautico.clubnautico.repository.userRepository;
import com.clubnautico.clubnautico.Exception.NotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AauthService {

    private final userRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(RegisterRequest request) {
        var existingUser = repository.findByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            throw new NotFound("El nombre de usuario ya está en uso");
        }

        var user = User.builder()
                .name(request.getName())
                .lastname(request.getLastname())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .esPatron(request.getIsPatron())
                .build();

        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwtToken).build();
    }


    @Override
    public AuthResponse authenticate(AuthenticateRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));
        var user = repository.findByUsername(request.getUsername()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwtToken).build();
    }
}
