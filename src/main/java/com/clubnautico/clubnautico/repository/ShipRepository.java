package com.clubnautico.clubnautico.repository;


import com.clubnautico.clubnautico.entity.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ShipRepository extends JpaRepository<Ship, Long> {
    List<Ship> findByPropietarioId(Long propietarioId); // Buscar barcos por el ID del propietario
}
