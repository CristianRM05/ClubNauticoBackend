package com.clubnautico.clubnautico.controller;

import com.clubnautico.clubnautico.Service.ShipService;
import com.clubnautico.clubnautico.controller.Models.BarcoRequest;
import com.clubnautico.clubnautico.controller.Models.ShiRsponse;
import com.clubnautico.clubnautico.entity.Ship;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/barcos")
@RequiredArgsConstructor
public class ShipController {

    private final ShipService barcoService;

    @GetMapping("/traer")
    public ResponseEntity<List<ShiRsponse>> getUserBarcos() {
        return ResponseEntity.ok(barcoService.getUserBarcos());
    }

    @PostMapping("/register")
    public ResponseEntity<Ship> createBarco(@RequestBody BarcoRequest barcoRequest) {
        return ResponseEntity.ok(barcoService.createBarco(barcoRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBarco(@PathVariable Long id) {
        barcoService.deleteBarco(id);
        return ResponseEntity.noContent().build();
    }
}
