package com.projetimmo.projet_immobilier.controller;

import com.projetimmo.projet_immobilier.dto.BienRequest;
import com.projetimmo.projet_immobilier.entity.Bien;
import com.projetimmo.projet_immobilier.service.interfaces.BienService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/biens")
@RequiredArgsConstructor
public class BienController {

    private final BienService bienService;

    // ➕ Créer un bien (PROPRIETAIRE)
    @PreAuthorize("hasRole('PROPRIETAIRE')")
    @PostMapping
    public ResponseEntity<Bien> creerBien(@RequestBody BienRequest request) {
        return ResponseEntity.ok(bienService.creerBien(request));
    }

    // 🔐 Lister MES biens (PROPRIETAIRE)
    @PreAuthorize("hasRole('PROPRIETAIRE')")
    @GetMapping("/mes-biens")
    public ResponseEntity<List<Bien>> listerMesBiens() {
        return ResponseEntity.ok(bienService.listerMesBiens());
    }

    // 🔓 Lister tous les biens (public ou authentifié)
    @GetMapping
    public ResponseEntity<List<Bien>> listerTous() {
        return ResponseEntity.ok(bienService.listerBiens());
    }

    // ✏️ Modifier un bien (PROPRIETAIRE)
    @PreAuthorize("hasRole('PROPRIETAIRE')")
    @PutMapping("/{id}")
    public ResponseEntity<Bien> modifierBien(
            @PathVariable Long id,
            @RequestBody BienRequest request) {
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
