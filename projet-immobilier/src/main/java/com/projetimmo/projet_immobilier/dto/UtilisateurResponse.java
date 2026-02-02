package com.projetimmo.projet_immobilier.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class UtilisateurResponse {

    private UUID id;
    private String prenom;
    private String nom;
    private String email;
    private String telephone;
    private String nomUtilisateur;
    private String role;
    private LocalDateTime createdAt;
}
