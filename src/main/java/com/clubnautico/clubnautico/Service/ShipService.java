package com.clubnautico.clubnautico.Service;

import com.clubnautico.clubnautico.Exception.NotFound;
import com.clubnautico.clubnautico.controller.Models.BarcoRequest;

import com.clubnautico.clubnautico.controller.Models.ShiRsponse;
import com.clubnautico.clubnautico.entity.Ship;
import com.clubnautico.clubnautico.entity.User;

import com.clubnautico.clubnautico.repository.ShipRepository;
import com.clubnautico.clubnautico.repository.userRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipService {

    private final ShipRepository barcoRepository;
    private final userRepository userRepository;

    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFound("Usuario no encontrado: " + username));
    }

    public List<ShiRsponse> getUserBarcos() {
        // Obtén el usuario autenticado
        User propietario = getAuthenticatedUser();

        List<Ship> barcos = barcoRepository.findByPropietarioId(propietario.getId());
        return barcos.stream()
                .map(barco -> new ShiRsponse(
                        barco.getId(),
                        barco.getName(),
                        barco.getModel(),
                        barco.getFee(),
                        barco.getPropietario().getId()
                ))
                .collect(Collectors.toList());
    }


    public Ship createBarco(BarcoRequest barcoRequest) {
        User propietario = getAuthenticatedUser();

        Ship barco = Ship.builder()
                .name(barcoRequest.getName())
                .model(barcoRequest.getModel())
                .fee(barcoRequest.getFee())
                .propietario(propietario)
                .build();

        return barcoRepository.save(barco);
    }

    public void deleteBarco(Long id) {
        User propietario = getAuthenticatedUser();
        Ship barco = barcoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Barco no encontrado con ID: " + id));

        if (!barco.getPropietario().equals(propietario)) {
            throw new RuntimeException("No tienes permiso para eliminar este barco.");
        }

        barcoRepository.delete(barco);
    }
}
