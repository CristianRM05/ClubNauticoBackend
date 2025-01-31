package com.clubnautico.clubnautico.Service;

import com.clubnautico.clubnautico.Exception.GlobalEcxception;
import com.clubnautico.clubnautico.controller.Models.ShipRequest;

import com.clubnautico.clubnautico.controller.Models.ShiRsponse;
import com.clubnautico.clubnautico.entity.Role;
import com.clubnautico.clubnautico.entity.Ship;
import com.clubnautico.clubnautico.entity.Trip;
import com.clubnautico.clubnautico.entity.User;

import com.clubnautico.clubnautico.repository.ShipRepository;
import com.clubnautico.clubnautico.repository.TripRepository;
import com.clubnautico.clubnautico.repository.UserRepository;
import jakarta.transaction.Transactional;
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
    private final TripRepository tripRepository; // Inyectamos el repositorio de Trip

    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new GlobalEcxception("Usuario no encontrado: " + username));
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
            throw new GlobalEcxception("Solo los miembros pueden crear barcos");
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

    @Transactional
    public void deleteBarco(Long id) {
        User propietario = getAuthenticatedUser();
        Ship barco = barcoRepository.findById(id)
                .orElseThrow(() -> new GlobalEcxception("Barco no encontrado con ID: " + id));

        if (!barco.getPropietario().equals(propietario)) {
            throw new RuntimeException("No tienes permiso para eliminar este barco.");
        }

        // Eliminar los viajes asociados al barco
        deleteTripsByShipId(id);

        // Eliminar el barco
        barcoRepository.delete(barco);
    }

    // Método para eliminar los viajes asociados a un barco
    private void deleteTripsByShipId(Long shipId) {
        List<Trip> trips = tripRepository.findByBarcoId(shipId);
        if (!trips.isEmpty()) {
            tripRepository.deleteAll(trips);
        }
    }


    @Transactional
    public Ship updateBarco(Long id, ShipRequest barcoRequest) {
        User propietario = getAuthenticatedUser();
        Ship barco = barcoRepository.findById(id)
                .orElseThrow(() -> new GlobalEcxception("Barco no encontrado con ID: " + id));
        barco.setName(barcoRequest.getName());
        barco.setMatricula(barcoRequest.getMatricula());

        return barcoRepository.save(barco);
    }

}
