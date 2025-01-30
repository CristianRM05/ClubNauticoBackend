package com.clubnautico.clubnautico.Service;

import com.clubnautico.clubnautico.Exception.NotFound;
import com.clubnautico.clubnautico.controller.Models.UserResponse;
import com.clubnautico.clubnautico.entity.User;
import com.clubnautico.clubnautico.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public UserResponse getAuthenticatedUser() {
        // Obtener usuario autenticado
        User user = repository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new NotFound("Usuario no encontrado"));

        return new UserResponse(
                user.getName(),
                user.getLastname(),
                user.getUsername(),
                user.getRole(),
                user.isEsPatron()
        );
    }


    @Transactional
    public UserResponse updateAuthenticatedUser(UserResponse updatedUser) {
        User user = repository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new NotFound("Usuario no encontrado"));
        user.setName(updatedUser.getName());
        user.setLastname(updatedUser.getLastname());
        user.setUsername(updatedUser.getUsername());

        repository.save(user);
        return new UserResponse(user.getName(), user.getLastname(), user.getUsername(), user.getRole(), user.isEsPatron());
    }
}
