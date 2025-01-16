package com.clubnautico.clubnautico.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/saludo")
public class SaludaController {

    @GetMapping("/saludapublico")
    public String saluda() {
        return "saluda";
    }
    @GetMapping("/saludaprivado")
    public String saludaSeguridad() {
        return "saluda Seguro";
    }
}
