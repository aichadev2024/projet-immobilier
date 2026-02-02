package com.projetimmo.projet_immobilier.service.interfaces;

import com.projetimmo.projet_immobilier.dto.LoginRequest;
import com.projetimmo.projet_immobilier.dto.LoginResponse;
import com.projetimmo.projet_immobilier.dto.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}
