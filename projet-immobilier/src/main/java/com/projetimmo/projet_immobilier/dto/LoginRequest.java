package com.projetimmo.projet_immobilier.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank
    private String nomUtilisateur;

    @NotBlank
    private String motDePasse;
}
