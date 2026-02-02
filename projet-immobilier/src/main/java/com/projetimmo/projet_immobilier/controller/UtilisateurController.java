package com.projetimmo.projet_immobilier.controller;

import com.projetimmo.projet_immobilier.dto.UtilisateurCreateRequest;
import com.projetimmo.projet_immobilier.dto.UtilisateurResponse;
import com.projetimmo.projet_immobilier.entity.Utilisateur;
import com.projetimmo.projet_immobilier.enums.StatutUtilisateur;
import com.projetimmo.projet_immobilier.service.interfaces.UtilisateurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @PostMapping
    public UtilisateurResponse creerUtilisateur(
            @Valid @RequestBody UtilisateurCreateRequest request) {

        Utilisateur utilisateur = Utilisateur.builder()
                .prenom(request.getPrenom())
                .nom(request.getNom())
                .email(request.getEmail())
                .telephone(request.getTelephone())
                .nomUtilisateur(request.getNomUtilisateur())
                .motDePasse(request.getMotDePasse())
                .statut(StatutUtilisateur.ACTIF)

                .build();

        Utilisateur saved = utilisateurService.creerUtilisateur(utilisateur);

        return UtilisateurResponse.builder()
                .id(saved.getId())
                .prenom(saved.getPrenom())
                .nom(saved.getNom())
                .email(saved.getEmail())
                .telephone(saved.getTelephone())
                .nomUtilisateur(saved.getNomUtilisateur())
                .role(saved.getRole().getNom())
                .createdAt(saved.getCreatedAt())
                .build();
    }
}
