package com.clubnautico.clubnautico.Service;

import com.clubnautico.clubnautico.Exception.NotFound;
import com.clubnautico.clubnautico.controller.Models.ShipRequest;

import com.clubnautico.clubnautico.controller.Models.ShiRsponse;
import com.clubnautico.clubnautico.entity.Role;
import com.clubnautico.clubnautico.entity.Ship;
import com.clubnautico.clubnautico.entity.User;

import com.clubnautico.clubnautico.repository.ShipRepository;
import com.clubnautico.clubnautico.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipService {

    private final ShipRepository barcoRepository;
    private final UserRepository userRepository;

    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFound("Usuario no encontrado: " + username));
    }

    public List<ShiRsponse> getUserBarcos() {
        User propietario = getAuthenticatedUser();
        List<Ship> barcos = barcoRepository.findByPropietarioId(propietario.getId());
        return barcos.stream()
                .map(barco -> new ShiRsponse(
                        barco.getId(),
                        barco.getName(),
                        barco.getMatricula(),
                        barco.getAmarre(),
                        barco.getFee(),
                        barco.getPropietario().getId()
                ))
                .collect(Collectors.toList());
    }


    public Ship createBarco(ShipRequest barcoRequest) {
        User propietario = getAuthenticatedUser();
        if (propietario.getRole() != Role.MEMBER) {
            throw new NotFound("Solo los miembros pueden crear barcos");
        }
        Ship barco = Ship.builder()
                .name(barcoRequest.getName())
                .matricula(barcoRequest.getMatricula())
                .amarre(barcoRequest.getAmarre())
                .fee(barcoRequest.getFee())
                .propietario(propietario)
                .build();

        return barcoRepository.save(barco);
    }

    public void deleteBarco(Long id) {
        User propietario = getAuthenticatedUser();
        Ship barco = barcoRepository.findById(id)
                .orElseThrow(() -> new NotFound("Barco no encontrado con ID: " + id));

        if (!barco.getPropietario().equals(propietario)) {
            throw new RuntimeException("No tienes permiso para eliminar este barco.");
        }

        barcoRepository.delete(barco);
    }
}
