package com.projetimmo.projet_immobilier.service.implementation;

import com.projetimmo.projet_immobilier.dto.LoginRequest;
import com.projetimmo.projet_immobilier.dto.LoginResponse;
import com.projetimmo.projet_immobilier.dto.RegisterRequest;
import com.projetimmo.projet_immobilier.entity.RefreshToken;
import com.projetimmo.projet_immobilier.entity.Role;
import com.projetimmo.projet_immobilier.entity.Utilisateur;
import com.projetimmo.projet_immobilier.enums.StatutUtilisateur;
import com.projetimmo.projet_immobilier.repository.RefreshTokenRepository;
import com.projetimmo.projet_immobilier.repository.RoleRepository;
import com.projetimmo.projet_immobilier.repository.UtilisateurRepository;
import com.projetimmo.projet_immobilier.security.JwtService;
import com.projetimmo.projet_immobilier.service.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

        private final UtilisateurRepository utilisateurRepository;
        private final RoleRepository roleRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final RefreshTokenRepository refreshTokenRepository;

        @Override
        public void register(RegisterRequest request) {

                if (utilisateurRepository.existsByNomUtilisateur(request.getNomUtilisateur())) {
                        throw new RuntimeException("Nom d'utilisateur déjà utilisé");
                }

                Role role = roleRepository
                                .findByNom(request.getRole().name())
                                .orElseThrow(() -> new RuntimeException("Rôle introuvable"));

                Utilisateur utilisateur = Utilisateur.builder()
                                .nom(request.getNom())
                                .prenom(request.getPrenom())
                                .nomUtilisateur(request.getNomUtilisateur())
                                .email(request.getEmail())
                                .telephone(request.getTelephone())
                                .motDePasse(passwordEncoder.encode(request.getMotDePasse()))
                                .statut(StatutUtilisateur.ACTIF)
                                .role(role)
                                .isDeleted(false)
                                .build();

                utilisateurRepository.save(Objects.requireNonNull(utilisateur));
        }

        @Override
        public LoginResponse login(LoginRequest request) {

                Utilisateur utilisateur = utilisateurRepository
                                .findByNomUtilisateur(request.getNomUtilisateur())
                                .orElseThrow(() -> new RuntimeException("Identifiants invalides"));

                if (utilisateur.getStatut() != StatutUtilisateur.ACTIF) {
                        throw new RuntimeException("Compte inactif");
                }

                boolean match = passwordEncoder.matches(
                                request.getMotDePasse(),
                                utilisateur.getMotDePasse());

                System.out.println("🔐 PASSWORD MATCH = " + match);

                if (!match) {
                        throw new RuntimeException("Identifiants invalides");
                }

                String accessToken = jwtService.generateToken(utilisateur.getNomUtilisateur());

                String refreshTokenValue = UUID.randomUUID().toString();

                RefreshToken refreshToken = RefreshToken.builder()
                                .token(refreshTokenValue)
                                .utilisateur(utilisateur)
                                .expiration(LocalDateTime.now().plusDays(30))
                                .revoked(false)
                                .build();

                refreshTokenRepository.save(
                                Objects.requireNonNull(refreshToken));

                return new LoginResponse(accessToken, refreshTokenValue);

        }

        @Override
        public void logout(String refreshTokenValue) {

                RefreshToken refreshToken = refreshTokenRepository
                                .findByToken(refreshTokenValue)
                                .orElseThrow(() -> new RuntimeException("Token invalide"));

                refreshToken.setRevoked(true);
                refreshTokenRepository.save(refreshToken);
        }

        @Override
        public LoginResponse refreshToken(String refreshTokenValue) {

                RefreshToken refreshToken = refreshTokenRepository
                                .findByToken(refreshTokenValue)
                                .orElseThrow(() -> new RuntimeException("Refresh token invalide"));

                if (refreshToken.isRevoked()) {
                        throw new RuntimeException("Refresh token révoqué");
                }

                if (refreshToken.getExpiration().isBefore(LocalDateTime.now())) {
                        throw new RuntimeException("Refresh token expiré");
                }

                Utilisateur utilisateur = refreshToken.getUtilisateur();

                if (utilisateur.getStatut() != StatutUtilisateur.ACTIF) {
                        throw new RuntimeException("Compte inactif");
                }

                String newAccessToken = jwtService.generateToken(utilisateur.getNomUtilisateur());

                return new LoginResponse(newAccessToken, refreshTokenValue);
        }

}
