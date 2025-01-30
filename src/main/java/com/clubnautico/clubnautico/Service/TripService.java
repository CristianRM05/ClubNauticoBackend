package com.clubnautico.clubnautico.Service;

import com.clubnautico.clubnautico.Exception.NotFound;
import com.clubnautico.clubnautico.controller.Models.TripRequest;
import com.clubnautico.clubnautico.controller.Models.TripResponse;
import com.clubnautico.clubnautico.entity.*;

import com.clubnautico.clubnautico.repository.ShipRepository;
import com.clubnautico.clubnautico.repository.TripRepository;

import com.clubnautico.clubnautico.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripService {

    @Autowired
    private TripRepository tripRepository;
@Autowired
  private UserRepository userRepository;

    @Autowired
    private ShipRepository shipRepository;

    private User getAuthenticateUser(){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return userRepository.findByUsername(username)
                .orElseThrow(()->new NotFound("usuario no encontrado: " + username));
    }

    @Transactional
    public TripResponse createTrip(TripRequest request) {
        User usuarioActual = getAuthenticateUser();

        if (usuarioActual.getRole() != Role.MEMBER) {
            throw new NotFound("Solo los miembros pueden crear viajes");
        }
        Ship barco = shipRepository.findById(request.getShipId())
                .orElseThrow(() -> new NotFound("Barco no encontrado"));

        User patron = null;
        if (request.getPatronId() != null) { // Solo buscamos si el ID no es null
            patron = userRepository.findById(request.getPatronId())
                    .orElseThrow(() -> new NotFound("Patrón no encontrado"));

            if (!patron.isEsPatron()) { // Validamos que sea un patrón
                throw new RuntimeException("El usuario seleccionado no es un patrón");
            }
        }

        Trip trip = Trip.builder()
                .fechayHora(request.getFechayHora())
                .descripcion(request.getDescription())
                .organizadorId(usuarioActual)
                .barco(barco)
                .patron(patron) // Puede ser null
                .tripRole(TripRole.PENDING)
                .build();

        Trip savedTrip = tripRepository.save(trip);
        return toResponse(savedTrip);
    }


    public List<TripResponse> getAllTrips() {
        User usuarioActual = getAuthenticateUser(); // Obtener usuario autenticado

        return tripRepository.findByOrganizadorId(usuarioActual).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


    public TripResponse getALL() {
        TripRole role = TripRole.PENDING;
        Trip trip = (Trip) tripRepository.findByTripRole(role);


        return toResponse(trip);
    }

    public Trip getTripById(Long id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new NotFound("Viaje no encontrado con ID: " + id));
    }



    @Transactional
    public void deleteTrip(Long id) {
        if (!tripRepository.existsById(id)) {
            throw new RuntimeException("Trip no encontrado");
        }
        tripRepository.deleteById(id);
    }

    public TripResponse toResponse(Trip trip) {
        TripResponse response = new TripResponse();
        response.setIdTrip(trip.getIdTrip());
        response.setFechayHora(trip.getFechayHora());
        response.setDescription(trip.getDescripcion());
        response.setOrganizadorName(trip.getOrganizadorId().getName());
        response.setTripRole(trip.getTripRole());
        response.setBarcoId(trip.getBarco().getId());

        if (trip.getPatron() != null) {
            response.setPatronId(trip.getPatron().getId());
            response.setPatronName(trip.getPatron().getName());
        } else {
            response.setPatronId(null);
            response.setPatronName("Sin asignar");
        }

        return response;
    }





}
