package com.projetimmo.projet_immobilier.service.implementation;

import com.projetimmo.projet_immobilier.entity.Role;
import com.projetimmo.projet_immobilier.entity.Utilisateur;
import com.projetimmo.projet_immobilier.enums.RoleType;
import com.projetimmo.projet_immobilier.repository.RoleRepository;
import com.projetimmo.projet_immobilier.repository.UtilisateurRepository;
import com.projetimmo.projet_immobilier.service.interfaces.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Utilisateur creerUtilisateur(Utilisateur utilisateur) {

        // 🔎 Vérifier unicité email
        if (utilisateurRepository.findByEmail(utilisateur.getEmail()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé");
        }

        Role role = roleRepository.findByNom(RoleType.CLIENT.name())
                .orElseThrow(() -> new RuntimeException("Rôle CLIENT introuvable"));

        // 🔐 Hash du mot de passe
        utilisateur.setMotDePasse(
                passwordEncoder.encode(utilisateur.getMotDePasse()));

        utilisateur.setRole(role);
        utilisateur.setCreatedAt(LocalDateTime.now());

        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Utilisateur getUtilisateurParId(@NonNull UUID id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }

    @Override
    public List<Utilisateur> listerUtilisateurs() {
        return utilisateurRepository.findAllByIsDeletedFalse();
    }

    @Override
    public Utilisateur supprimerUtilisateur(@NonNull UUID id) {
        Utilisateur utilisateur = getUtilisateurParId(id);
        utilisateur.setIsDeleted(true);
        utilisateur.setDeletedAt(LocalDateTime.now());
        return utilisateurRepository.save(utilisateur);
    }
}
