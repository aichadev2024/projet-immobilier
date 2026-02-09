package com.projetimmo.projet_immobilier.service.implementation;

import com.projetimmo.projet_immobilier.dto.BienRequest;
import com.projetimmo.projet_immobilier.entity.Bien;
import com.projetimmo.projet_immobilier.entity.TypeBien;
import com.projetimmo.projet_immobilier.entity.Utilisateur;
import com.projetimmo.projet_immobilier.enums.ModeTarification;
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

        Objects.requireNonNull(request, "La requête est obligatoire");
        Objects.requireNonNull(request.getIdTypeBien(), "Type de bien obligatoire");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("Utilisateur non authentifié");
        }

        Utilisateur utilisateur = utilisateurRepository
                .findByNomUtilisateur(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (!RoleType.PROPRIETAIRE.name().equals(utilisateur.getRole().getNom())) {
            throw new RuntimeException("Seul un propriétaire peut créer un bien");
        }

        Long idTypeBien = Objects.requireNonNull(
                request.getIdTypeBien(),
                "Type de bien obligatoire");

        TypeBien typeBien = typeBienRepository
                .findById(idTypeBien)
                .orElseThrow(() -> new RuntimeException("Type de bien introuvable"));

        Double prixCalculer;

        if (typeBien.getModeTarification() == ModeTarification.AU_M2) {

            if (request.getSuperficie() == null) {
                throw new RuntimeException("La superficie est obligatoire pour un bien au m²");
            }

            prixCalculer = request.getSuperficie() * typeBien.getTarifBase();

        } else if (typeBien.getModeTarification() == ModeTarification.FORFAIT) {

            prixCalculer = typeBien.getTarifBase();

        } else {
            throw new RuntimeException("Mode de tarification non pris en charge");
        }

        Bien bien = Objects.requireNonNull(
                Bien.builder()
                        .libelle(request.getLibelle())
                        .description(request.getDescription())
                        .superficie(request.getSuperficie())
                        .latitude(request.getLatitude())
                        .longitude(request.getLongitude())
                        .adresse(request.getAdresse())
                        .statutBien(request.getStatutBien())
                        .prixCalculer(prixCalculer)
                        .utilisateur(utilisateur)
                        .typeBien(typeBien)
                        .build(),
                "Erreur lors de la création du bien");

        return bienRepository.save(bien);

    }

    // ===================== LISTER MES BIENS =====================
    @Override
    public List<Bien> listerMesBiens() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Utilisateur non authentifié");
        }

        Utilisateur utilisateur = utilisateurRepository
                .findByNomUtilisateur(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        if (!RoleType.PROPRIETAIRE.name().equals(utilisateur.getRole().getNom())) {
            throw new RuntimeException("Accès refusé");
        }

        return bienRepository.findByUtilisateurAndIsDeletedFalse(utilisateur);
    }

    // ===================== MODIFIER BIEN =====================
    @Override
    public Bien modifierBien(Long idBien, BienRequest request) {

        Objects.requireNonNull(idBien, "Id du bien obligatoire");
        Objects.requireNonNull(request, "Requête obligatoire");

        Utilisateur utilisateur = utilisateurRepository
                .findByNomUtilisateur(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Bien bien = bienRepository
                .findById(idBien)
                .orElseThrow(() -> new RuntimeException("Bien introuvable"));

        if (!bien.getUtilisateur().getId().equals(utilisateur.getId())) {
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

        Objects.requireNonNull(idBien, "Id du bien obligatoire");

        Utilisateur utilisateur = utilisateurRepository
                .findByNomUtilisateur(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        Bien bien = bienRepository
                .findById(idBien)
                .orElseThrow(() -> new RuntimeException("Bien introuvable"));

        if (!bien.getUtilisateur().getId().equals(utilisateur.getId())) {
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
