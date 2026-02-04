package com.projetimmo.projet_immobilier.controller;

import com.projetimmo.projet_immobilier.dto.BienRequest;
import com.projetimmo.projet_immobilier.entity.Bien;
import com.projetimmo.projet_immobilier.service.interfaces.BienService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/biens")
@RequiredArgsConstructor
public class BienController {

    private final BienService bienService;

    @PostMapping
    public ResponseEntity<Bien> creerBien(@RequestBody BienRequest request) {
        return ResponseEntity.ok(bienService.creerBien(request));
    }

    @GetMapping
    public ResponseEntity<List<Bien>> lister() {
        return ResponseEntity.ok(bienService.listerBiens());
    }
}
