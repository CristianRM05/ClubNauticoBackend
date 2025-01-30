package com.clubnautico.clubnautico.controller;

import com.clubnautico.clubnautico.Service.ShipService;
import com.clubnautico.clubnautico.controller.Models.ShipRequest;
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
    public ResponseEntity<Ship> createBarco(@RequestBody ShipRequest barcoRequest) {
        return ResponseEntity.ok(barcoService.createBarco(barcoRequest));
    }

    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<Void> deleteBarco(@PathVariable Long id) {
        barcoService.deleteBarco(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Ship> updateBarco(@PathVariable Long id, @RequestBody ShipRequest barcoRequest) {
        return ResponseEntity.ok(barcoService.updateBarco(id, barcoRequest));
    }

}
