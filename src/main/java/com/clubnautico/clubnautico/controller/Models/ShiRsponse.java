package com.clubnautico.clubnautico.controller.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiRsponse {
    private Long id;
    private String name;
    private String matricula;
    private int amarre;
    private double fee;
    private Long propietarioId;
}
