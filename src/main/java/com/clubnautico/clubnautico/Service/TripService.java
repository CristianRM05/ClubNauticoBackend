package com.clubnautico.clubnautico.Service;

import com.clubnautico.clubnautico.Exception.GlobalEcxception;
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
                .orElseThrow(()->new GlobalEcxception("usuario no encontrado: " + username));
    }

    @Transactional
    public TripResponse createTrip(TripRequest request) {
        User usuarioActual = getAuthenticateUser();
        if (usuarioActual.getRole() != Role.MEMBER) {
            throw new GlobalEcxception("Solo los miembros pueden crear viajes");
        }

        Ship barco = shipRepository.findById(request.getShipId())
                .orElseThrow(() -> new GlobalEcxception("Barco no encontrado"));

        boolean hasOngoingTrip = tripRepository.existsByBarcoAndTripRoleNot(barco, TripRole.FINISHED);
        if (hasOngoingTrip) {
            throw new GlobalEcxception("Este barco ya tiene un viaje en curso que no ha terminado.");
        }
        User patron = null;
        if (request.getPatronId() != null) {
            patron = userRepository.findById(request.getPatronId())
                    .orElseThrow(() -> new GlobalEcxception("Patrón no encontrado"));
            if (!patron.isEsPatron()) {
                throw new GlobalEcxception("El usuario seleccionado no es un patrón");
            }
        }

        Trip trip = Trip.builder()
                .fechayHora(request.getFechayHora())
                .descripcion(request.getDescription())
                .organizadorId(usuarioActual)
                .barco(barco)
                .patron(patron)
                .tripRole(TripRole.PENDING)
                .build();

        Trip savedTrip = tripRepository.save(trip);
        return toResponse(savedTrip);
    }

    public List<TripResponse> getAllTrips() {
        User usuarioActual = getAuthenticateUser();

        return tripRepository.findByOrganizadorId(usuarioActual).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<TripResponse> getALL() {
        TripRole role = TripRole.PENDING;
        List<Trip> trips = tripRepository.findByTripRole(role);
        return trips.stream().map(this::toResponse).collect(Collectors.toList());
    }


    public Trip getTripById(Long id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new GlobalEcxception("Viaje no encontrado con ID: " + id));
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
        response.setShipMatricula(trip.getBarco().getMatricula());
        if (trip.getPatron() != null) {
            response.setPatronId(trip.getPatron().getId());
            response.setPatronName(trip.getPatron().getName());
        } else {
            response.setPatronId(null);
            response.setPatronName("Sin asignar");
        }

        return response;
    }

    @Transactional
    public TripResponse updateTrip(Long id, TripRequest request) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new GlobalEcxception("Viaje no encontrado con ID: " + id));

        if (trip.getTripRole() == TripRole.FINISHED) {
            throw new GlobalEcxception("El viaje ya ha sido finalizado y no puede ser actualizado.");
        }

        trip.setFechayHora(request.getFechayHora());
        trip.setDescripcion(request.getDescription());

        if (request.getShipId() != null && !request.getShipId().equals(trip.getBarco().getId())) {
            Ship barco = shipRepository.findById(request.getShipId())
                    .orElseThrow(() -> new GlobalEcxception("Barco no encontrado"));
            trip.setBarco(barco);
        }

        Trip updatedTrip = tripRepository.save(trip);

        return toResponse(updatedTrip);
    }

    @Transactional
    public TripResponse updateTripOrganizador(Long idTrip, Long idPatron) {
        Trip trip = tripRepository.findById(idTrip)
                .orElseThrow(() -> new GlobalEcxception("Viaje no encontrado con ID: " + idTrip));

        if (trip.getTripRole() == TripRole.FINISHED) {
            throw new GlobalEcxception("El viaje ya ha sido finalizado.");
        }
        User patron = userRepository.findById(idPatron)
                .orElseThrow(() -> new GlobalEcxception("Patrón no encontrado con ID: " + idPatron));

        if (!patron.isEsPatron()) {
            throw new GlobalEcxception("El usuario seleccionado no es un patrón.");
        }

        trip.setPatron(patron);
        trip.setTripRole(TripRole.FINISHED);

        Trip updatedTrip = tripRepository.save(trip);

        return toResponse(updatedTrip);
    }





}
