package com.clubnautico.clubnautico.controller.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiRsponse {
    private Long id;
    private String nombre;
    private String model;
    private int fee;
    private Long propietarioId;
}
