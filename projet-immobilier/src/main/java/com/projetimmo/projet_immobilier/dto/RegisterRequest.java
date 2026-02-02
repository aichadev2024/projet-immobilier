package com.projetimmo.projet_immobilier.dto;

import com.projetimmo.projet_immobilier.enums.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    @NotBlank
    private String nomUtilisateur;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String motDePasse;

    @NotBlank
    private String telephone;

    // choix contrôlé
    @NotNull
    private RoleType role; // ADMIN | CLIENT | PROPRIETAIRE
}
