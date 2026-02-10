package com.projetimmo.projet_immobilier.service.implementation;

import com.projetimmo.projet_immobilier.dto.AnnonceRequest;
import com.projetimmo.projet_immobilier.dto.AnnonceResponse;
import com.projetimmo.projet_immobilier.entity.Annonce;
import com.projetimmo.projet_immobilier.entity.Bien;
import com.projetimmo.projet_immobilier.entity.Utilisateur;
import com.projetimmo.projet_immobilier.repository.AnnonceRepository;
import com.projetimmo.projet_immobilier.repository.BienRepository;
import com.projetimmo.projet_immobilier.repository.UtilisateurRepository;
import com.projetimmo.projet_immobilier.service.interfaces.AnnonceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class AnnonceServiceImpl implements AnnonceService {

    private final AnnonceRepository annonceRepository;
    private final BienRepository bienRepository;
    private final UtilisateurRepository utilisateurRepository;

    // ================= UTILISATEUR CONNECTÉ =================
    private Utilisateur getUtilisateurConnecte() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Utilisateur non authentifié");
        }

        return utilisateurRepository
                .findByNomUtilisateur(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable"));
    }

    // ================= MAPPING =================
    private AnnonceResponse mapToResponse(Annonce annonce) {
        return AnnonceResponse.builder()
                .id(annonce.getId())
                .typeAnnonce(annonce.getTypeAnnonce())
                .statut(annonce.getStatut())
                .idBien(annonce.getBien().getId())
                .libelleBien(annonce.getBien().getLibelle())
                .build();
    }

    // ================= CRÉER ANNONCE =================
    @Override
    @PreAuthorize("hasRole('PROPRIETAIRE')")
    public AnnonceResponse creerAnnonce(AnnonceRequest request) {

        Utilisateur utilisateur = getUtilisateurConnecte();

        Bien bien = bienRepository.findById(
                Objects.requireNonNull(request.getIdBien(), "idBien obligatoire"))
                .orElseThrow(() -> new IllegalArgumentException("Bien introuvable"));

        if (!bien.getUtilisateur().getId().equals(utilisateur.getId())) {
            throw new SecurityException("Ce bien ne vous appartient pas");
        }

        Annonce annonce = Annonce.builder()
                .typeAnnonce(request.getTypeAnnonce())
                .statut(request.getStatut())
                .utilisateur(utilisateur)
                .bien(bien)
                .build();

        return mapToResponse(annonceRepository.save(Objects.requireNonNull(annonce)));
    }

    // ================= MES ANNONCES =================
    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<AnnonceResponse> listerMesAnnonces() {

        Utilisateur utilisateur = getUtilisateurConnecte();

        return annonceRepository
                .findByUtilisateurAndIsDeletedFalse(utilisateur)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ================= TOUTES LES ANNONCES =================
    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<AnnonceResponse> listerToutesAnnonces() {

        return annonceRepository
                .findByIsDeletedFalse()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ================= GET PAR ID =================
    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public AnnonceResponse getAnnonceById(Long id) {

        Annonce annonce = annonceRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new IllegalArgumentException("Annonce introuvable"));

        return mapToResponse(annonce);
    }

    // ================= SUPPRESSION =================
    @Override
    @PreAuthorize("hasRole('PROPRIETAIRE')")
    public void supprimerAnnonce(Long id) {

        Utilisateur utilisateur = getUtilisateurConnecte();

        Annonce annonce = annonceRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new IllegalArgumentException("Annonce introuvable"));

        if (!annonce.getUtilisateur().getId().equals(utilisateur.getId())) {
            throw new SecurityException("Suppression non autorisée");
        }

        annonce.setIsDeleted(true);
        annonceRepository.save(annonce);
    }
}
