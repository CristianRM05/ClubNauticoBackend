package com.clubnautico.clubnautico.Service;

import com.clubnautico.clubnautico.Exception.NotFound;
import com.clubnautico.clubnautico.controller.Models.TripRequest;
import com.clubnautico.clubnautico.controller.Models.TripResponse;
import com.clubnautico.clubnautico.entity.Trip;
import com.clubnautico.clubnautico.entity.User;

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

    private User getAuthenticateUser(){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return userRepository.findByUsername(username)
                .orElseThrow(()->new NotFound("usuario no encontrado: " + username));
    }

    @Transactional
    public TripResponse createTrip(TripRequest request) {

        User organizador = getAuthenticateUser();



        Trip trip = new Trip();
        trip.setFechayHora(request.getFechayHora());
        trip.setDescripcion(request.getDescription());
        trip.setOrganizadorId(organizador);

        Trip savedTrip = tripRepository.save(trip);

        return toResponse(savedTrip);
    }

    public List<TripResponse> getAllTrips() {
        return tripRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TripResponse getTripById(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip no encontrado"));
        return toResponse(trip);
    }

    @Transactional
    public TripResponse updateTrip(Long id, TripRequest request) {
        User organizador = getAuthenticateUser();
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip no encontrado"));

         organizador = userRepository.findById(organizador.getId())
                .orElseThrow(() -> new RuntimeException("Organizador no encontrado"));

        trip.setFechayHora(request.getFechayHora());
        trip.setDescripcion(request.getDescription());
        trip.setOrganizadorId(organizador);

        Trip updatedTrip = tripRepository.save(trip);
        return toResponse(updatedTrip);
    }

    @Transactional
    public void deleteTrip(Long id) {
        if (!tripRepository.existsById(id)) {
            throw new RuntimeException("Trip no encontrado");
        }
        tripRepository.deleteById(id);
    }

    private TripResponse toResponse(Trip trip) {
        TripResponse response = new TripResponse();
        response.setIdTrip(trip.getIdTrip());
        response.setFechayHora(trip.getFechayHora());
        response.setDescription(trip.getDescripcion());
        response.setOrganizadorName(trip.getOrganizadorId().getName()); // Suponiendo que User tiene un campo "nombre"
        return response;
    }


}
