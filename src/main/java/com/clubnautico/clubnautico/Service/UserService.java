package com.clubnautico.clubnautico.Service;

import com.clubnautico.clubnautico.Exception.GlobalEcxception;
import com.clubnautico.clubnautico.controller.Models.UserResponse;
import com.clubnautico.clubnautico.entity.User;
import com.clubnautico.clubnautico.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public UserResponse getAuthenticatedUser() {
        User user = repository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new GlobalEcxception("Usuario no encontrado"));

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getLastname(),
                user.getUsername(),
                user.getRole(),
                user.isEsPatron()
        );
    }

    @Transactional
    public UserResponse updateAuthenticatedUser(UserResponse updatedUser) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = repository.findByUsername(currentUsername)
                .orElseThrow(() -> new GlobalEcxception("Usuario no encontrado"));

        if (!user.getUsername().equals(updatedUser.getUsername()) &&
                repository.findByUsername(updatedUser.getUsername()).isPresent()) {
            throw new GlobalEcxception("El nombre de usuario ya está en uso");
        }

        user.setName(updatedUser.getName());
        user.setLastname(updatedUser.getLastname());
        user.setUsername(updatedUser.getUsername());

        repository.save(user);
        return new UserResponse(user.getId(),user.getName(), user.getLastname(), user.getUsername(), user.getRole(), user.isEsPatron());
    }
    public List<UserResponse> getPatrons() {
        return repository.findByEsPatronTrue().stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getLastname(),
                        user.getUsername(),
                        user.getRole(),
                        user.isEsPatron()
                ))
                .collect(Collectors.toList());
    }

}
