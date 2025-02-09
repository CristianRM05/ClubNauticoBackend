package com.clubnautico.clubnautico.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTrip;

    private Date fechayHora;
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizadorId", nullable = false)
    private User organizadorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barcoId", nullable = false)
    private Ship barco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patronId", nullable = true)
    private User patron;

    @Enumerated(EnumType.ORDINAL)
    private TripRole tripRole;
}
