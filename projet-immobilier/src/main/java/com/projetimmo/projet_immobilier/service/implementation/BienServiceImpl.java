package com.projetimmo.projet_immobilier.service.implementation;

import com.projetimmo.projet_immobilier.dto.BienRequest;
import com.projetimmo.projet_immobilier.dto.BienResponse;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BienServiceImpl implements BienService {

    private final BienRepository bienRepository;
    private final TypeBienRepository typeBienRepository;
    private final UtilisateurRepository utilisateurRepository;

    // ===================== UTILISATEUR CONNECTÉ =====================
    private Utilisateur getUtilisateurConnecte() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("Utilisateur non authentifié");
        }

        return utilisateurRepository
                .findByNomUtilisateur(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
    }

    // ===================== CALCUL PRIX =====================
    private BigDecimal calculerPrix(TypeBien typeBien, Double superficie) {

        switch (typeBien.getModeTarification()) {
            case AU_M2:
                if (superficie == null) {
                    throw new IllegalArgumentException("Superficie obligatoire");
                }
                return typeBien.getTarifBase()
                        .multiply(BigDecimal.valueOf(superficie));

            case FORFAIT:
                return typeBien.getTarifBase();

            default:
                throw new IllegalStateException("Mode de tarification inconnu");
        }
    }

    // ===================== MAPPING RESPONSE =====================
    private BienResponse mapToResponse(Bien bien) {

        return BienResponse.builder()
                .id(bien.getId())
                .libelle(bien.getLibelle())
                .description(bien.getDescription())
                .superficie(bien.getSuperficie())
                .latitude(bien.getLatitude())
                .longitude(bien.getLongitude())
                .adresse(bien.getAdresse())
                .prixCalcule(
                        bien.getPrixCalculer() != null
                                ? bien.getPrixCalculer().doubleValue()
                                : null)
                .statutBien(bien.getStatutBien())
                .idTypeBien(bien.getTypeBien().getId())
                .libelleTypeBien(bien.getTypeBien().getLibelle())
                .build();
    }

    // ===================== CRÉER BIEN =====================
    @Override
    public BienResponse creerBien(BienRequest request) {

        Utilisateur utilisateur = getUtilisateurConnecte();

        if (!RoleType.PROPRIETAIRE.name()
                .equals(utilisateur.getRole().getNom())) {
            throw new RuntimeException("Seul un propriétaire peut créer un bien");
        }

        TypeBien typeBien = typeBienRepository
                .findById(Objects.requireNonNull(request.getIdTypeBien()))
                .orElseThrow(() -> new RuntimeException("Type de bien introuvable"));

        BigDecimal prixCalculer = calculerPrix(typeBien, request.getSuperficie());

        Bien bien = Bien.builder()
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
                .build();

        return mapToResponse(bienRepository.save(Objects.requireNonNull(bien)));
    }

    // ===================== MES BIENS =====================
    @Override
    public List<BienResponse> listerMesBiens() {

        Utilisateur utilisateur = getUtilisateurConnecte();

        if (!RoleType.PROPRIETAIRE.name()
                .equals(utilisateur.getRole().getNom())) {
            throw new RuntimeException("Accès refusé");
        }

        return bienRepository
                .findByUtilisateurAndIsDeletedFalse(utilisateur)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ===================== MODIFIER BIEN =====================
    @Override
    public BienResponse modifierBien(Long idBien, BienRequest request) {

        Utilisateur utilisateur = getUtilisateurConnecte();

        Bien bien = bienRepository.findById(Objects.requireNonNull(idBien))
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

        return mapToResponse(bienRepository.save(bien));
    }

    // ===================== SUPPRESSION LOGIQUE =====================
    @Override
    public void supprimerBien(Long idBien) {

        Utilisateur utilisateur = getUtilisateurConnecte();

        Bien bien = bienRepository.findById(Objects.requireNonNull(idBien))
                .orElseThrow(() -> new RuntimeException("Bien introuvable"));

        if (!bien.getUtilisateur().getId().equals(utilisateur.getId())) {
            throw new RuntimeException("Vous ne pouvez pas supprimer ce bien");
        }

        bien.setIsDeleted(true);
        bienRepository.save(bien);
    }

    // ===================== TOUS LES BIENS =====================
    @Override
    public List<BienResponse> listerBiens() {

        return bienRepository.findByIsDeletedFalse()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
}
