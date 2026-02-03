package com.projetimmo.projet_immobilier.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private String refreshToken;

    // 🔹 constructeur pour ne rien casser
    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
        this.refreshToken = null;
    }
}
