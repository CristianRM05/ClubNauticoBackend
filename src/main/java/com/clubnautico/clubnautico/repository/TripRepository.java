package com.clubnautico.clubnautico.repository;

import com.clubnautico.clubnautico.entity.Ship;
import com.clubnautico.clubnautico.entity.Trip;
import com.clubnautico.clubnautico.entity.TripRole;
import com.clubnautico.clubnautico.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByOrganizadorId(User organizador);
    List<Trip> findByBarcoId(Long barcoId);
    List<Trip> findByTripRole(TripRole tripRole);

    boolean existsByBarcoAndTripRoleNot(Ship barco, TripRole tripRole);
}
