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
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BienServiceImpl implements BienService {

    private final BienRepository bienRepository;
    private final TypeBienRepository typeBienRepository;
    private final UtilisateurRepository utilisateurRepository;

    // ===================== CREER BIEN =====================
    @Override
    public Bien creerBien(BienRequest request) {

        if (request == null || request.getIdTypeBien() == null) {
            throw new IllegalArgumentException("Type de bien obligatoire");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("Utilisateur non authentifié");
        }

        Utilisateur utilisateur = utilisateurRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (!utilisateur.getRole().getNom().equals(RoleType.PROPRIETAIRE.name())) {
            throw new RuntimeException("Seul un propriétaire peut effectuer cette action");
        }

        TypeBien typeBien = typeBienRepository
                .findById(Objects.requireNonNull(request.getIdTypeBien()))
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

        Objects.requireNonNull(bien);
        return bienRepository.save(bien);
    }

    // ===================== LISTER MES BIENS =====================
    @Override
    public List<Bien> listerMesBiens() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("Utilisateur non authentifié");
        }

        Utilisateur utilisateur = utilisateurRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        if (!utilisateur.getRole().getNom().equals(RoleType.PROPRIETAIRE.name())) {
            throw new RuntimeException("Accès refusé");
        }

        return bienRepository.findByUtilisateurAndIsDeletedFalse(utilisateur);
    }

    // ===================== MODIFIER BIEN =====================
    @Override
    public Bien modifierBien(Long idBien, BienRequest request) {

        if (idBien == null) {
            throw new IllegalArgumentException("Id du bien obligatoire");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Utilisateur utilisateur = utilisateurRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Bien bien = bienRepository.findById(idBien)
                .orElseThrow(() -> new RuntimeException("Bien introuvable"));

        if (!Objects.equals(bien.getUtilisateur().getId(), utilisateur.getId())) {
            throw new RuntimeException("Vous ne pouvez pas modifier ce bien");
        }

        bien.setLibelle(request.getLibelle());
        bien.setDescription(request.getDescription());
        bien.setSuperficie(request.getSuperficie());
        bien.setLatitude(request.getLatitude());
        bien.setLongitude(request.getLongitude());
        bien.setAdresse(request.getAdresse());
        bien.setStatutBien(request.getStatutBien());

        return bienRepository.save(bien);
    }

    // ===================== SUPPRESSION LOGIQUE =====================
    @Override
    public void supprimerBien(Long idBien) {

        if (idBien == null) {
            throw new IllegalArgumentException("Id du bien obligatoire");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Utilisateur utilisateur = utilisateurRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Bien bien = bienRepository.findById(idBien)
                .orElseThrow(() -> new RuntimeException("Bien introuvable"));

        if (!Objects.equals(bien.getUtilisateur().getId(), utilisateur.getId())) {
            throw new RuntimeException("Vous ne pouvez pas supprimer ce bien");
        }

        bien.setIsDeleted(true);
        bienRepository.save(bien);
    }

    // ===================== LISTER TOUS LES BIENS =====================
    @Override
    public List<Bien> listerBiens() {
        return bienRepository.findByIsDeletedFalse();
    }
}
