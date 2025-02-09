package com.clubnautico.clubnautico.controller.Models;

import com.clubnautico.clubnautico.entity.TripRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripResponse {

    private Long idTrip;
    private Date fechayHora;
    private String description;
    private String organizadorName;
    private TripRole tripRole;
    private Long barcoId;
    private String patronName; // Nombre del patrón
    private Long patronId;
    private String shipMatricula;
}
