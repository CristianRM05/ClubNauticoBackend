package com.clubnautico.clubnautico.repository;

import com.clubnautico.clubnautico.entity.Trip;
import com.clubnautico.clubnautico.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findAllByOrganizadorId(User organizadorId);

}
