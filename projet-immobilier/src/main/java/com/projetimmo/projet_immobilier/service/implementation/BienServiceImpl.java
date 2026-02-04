package com.projetimmo.projet_immobilier.service.implementation;

import com.projetimmo.projet_immobilier.dto.BienRequest;
import com.projetimmo.projet_immobilier.entity.Bien;
import com.projetimmo.projet_immobilier.entity.TypeBien;
import com.projetimmo.projet_immobilier.entity.Utilisateur;
import com.projetimmo.projet_immobilier.enums.RoleType;
import com.projetimmo.projet_immobilier.repository.BienRepository;
import com.projetimmo.projet_immobilier.repository.TypeBienRepository;
import com.projetimmo.projet_immobilier.repository.UtilisateurRepository;
import com.projetimmo.projet_immobilier.service.interfaces.BienService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BienServiceImpl implements BienService {

    private final BienRepository bienRepository;
    private final TypeBienRepository typeBienRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Override
    public Bien creerBien(BienRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Utilisateur non authentifié");
        }

        String email = authentication.getName();

        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (!utilisateur.getRole().equals(RoleType.PROPRIETAIRE)) {
            throw new RuntimeException("Seul un propriétaire peut créer un bien");
        }

        TypeBien typeBien = typeBienRepository.findById(request.getIdTypeBien())
                .orElseThrow(() -> new RuntimeException("Type de bien introuvable"));

        Bien bien = Bien.builder()
                .libelle(request.getLibelle())
                .description(request.getDescription())
                .superficie(request.getSuperficie())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .prixCalculer(request.getPrixCalculer())
                .adresse(request.getAdresse())
                .statutBien(request.getStatutBien())
                .utilisateur(utilisateur)
                .typeBien(typeBien)
                .build();

        return bienRepository.save(bien);
    }

    @Override
    public List<Bien> listerBiens() {
        return bienRepository.findByIsDeletedFalse();
    }
}
