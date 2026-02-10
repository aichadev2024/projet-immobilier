package com.projetimmo.projet_immobilier.controller;

import com.projetimmo.projet_immobilier.dto.BienRequest;
import com.projetimmo.projet_immobilier.dto.BienResponse;
import com.projetimmo.projet_immobilier.service.interfaces.BienService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/biens")
@RequiredArgsConstructor
public class BienController {

    private final BienService bienService;

    // ➕ Créer un bien (PROPRIETAIRE)
    @PreAuthorize("hasRole('PROPRIETAIRE')")
    @PostMapping
    public ResponseEntity<BienResponse> creerBien(
            @Valid @RequestBody BienRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bienService.creerBien(request));
    }

    // 🔐 Lister MES biens (PROPRIETAIRE)
    @PreAuthorize("hasRole('PROPRIETAIRE')")
    @GetMapping("/mes-biens")
    public ResponseEntity<List<BienResponse>> listerMesBiens() {
        return ResponseEntity.ok(bienService.listerMesBiens());
    }

    // 🔓 Lister tous les biens (public)
    @GetMapping
    public ResponseEntity<List<BienResponse>> listerTous() {
        return ResponseEntity.ok(bienService.listerBiens());
    }

    // ✏️ Modifier un bien (PROPRIETAIRE)
    @PreAuthorize("hasRole('PROPRIETAIRE')")
    @PutMapping("/{id}")
    public ResponseEntity<BienResponse> modifierBien(
            @PathVariable Long id,
            @Valid @RequestBody BienRequest request) {

        return ResponseEntity.ok(bienService.modifierBien(id, request));
    }

    // 🗑️ Supprimer un bien (PROPRIETAIRE)
    @PreAuthorize("hasRole('PROPRIETAIRE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerBien(@PathVariable Long id) {
        bienService.supprimerBien(id);
        return ResponseEntity.noContent().build();
    }
}
