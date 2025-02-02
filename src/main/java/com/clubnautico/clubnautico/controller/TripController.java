package com.clubnautico.clubnautico.controller;

import com.clubnautico.clubnautico.Service.TripService;
import com.clubnautico.clubnautico.controller.Models.TripRequest;
import com.clubnautico.clubnautico.controller.Models.TripResponse;
import com.clubnautico.clubnautico.entity.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trip")
public class TripController {

    @Autowired
    private TripService tripService;

    @PostMapping("/create")
    public ResponseEntity<TripResponse> createTrip(@RequestBody TripRequest request) {
        TripResponse response = tripService.createTrip(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/obtener")
    public ResponseEntity<List<TripResponse>> getAllTrips() {
        List<TripResponse> trips = tripService.getAllTrips();
        return ResponseEntity.ok(trips);
    }
    @GetMapping("/obtener/{id}")
    public TripResponse getTripById(@PathVariable Long id) {
        Trip trip = tripService.getTripById(id); // Método en el servicio que obtiene el viaje por ID
        return tripService.toResponse(trip);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long id) {
        tripService.deleteTrip(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TripResponse updateTrip(@PathVariable Long id, @RequestBody TripRequest request) {
        return tripService.updateTrip(id, request);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<TripResponse>> getAllPendingTrips() {
        List<TripResponse> trips = tripService.getALL();
        return ResponseEntity.ok(trips);
    }
    @PutMapping("/updateOrganizador/{idTrip}/{idPatron}")
    public ResponseEntity<TripResponse> updateTripOrganizador(
            @PathVariable Long idTrip, @PathVariable Long idPatron) {
        return ResponseEntity.ok(tripService.updateTripOrganizador(idTrip, idPatron));
    }



}
