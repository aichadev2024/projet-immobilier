package com.projetimmo.projet_immobilier.service.implementation;

import com.projetimmo.projet_immobilier.dto.LoginRequest;
import com.projetimmo.projet_immobilier.dto.LoginResponse;
import com.projetimmo.projet_immobilier.dto.RegisterRequest;
import com.projetimmo.projet_immobilier.entity.Role;
import com.projetimmo.projet_immobilier.entity.Utilisateur;
import com.projetimmo.projet_immobilier.enums.StatutUtilisateur;
import com.projetimmo.projet_immobilier.repository.RoleRepository;
import com.projetimmo.projet_immobilier.repository.UtilisateurRepository;
import com.projetimmo.projet_immobilier.security.JwtService;
import com.projetimmo.projet_immobilier.service.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

        private final UtilisateurRepository utilisateurRepository;
        private final RoleRepository roleRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;

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

                String token = jwtService.generateToken(utilisateur.getNomUtilisateur());
                return new LoginResponse(token);
        }
}
