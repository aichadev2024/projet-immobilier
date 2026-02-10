package com.projetimmo.projet_immobilier.service.implementation;

import com.projetimmo.projet_immobilier.dto.AnnonceRequest;
import com.projetimmo.projet_immobilier.dto.AnnonceResponse;
import com.projetimmo.projet_immobilier.entity.Annonce;
import com.projetimmo.projet_immobilier.entity.Bien;
import com.projetimmo.projet_immobilier.entity.Utilisateur;
import com.projetimmo.projet_immobilier.enums.RoleType;
import com.projetimmo.projet_immobilier.repository.AnnonceRepository;
import com.projetimmo.projet_immobilier.repository.BienRepository;
import com.projetimmo.projet_immobilier.repository.UtilisateurRepository;
import com.projetimmo.projet_immobilier.service.interfaces.AnnonceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AnnonceServiceImpl implements AnnonceService {

    private final AnnonceRepository annonceRepository;
    private final BienRepository bienRepository;
    private final UtilisateurRepository utilisateurRepository;

    // ================= UTILISATEUR CONNECTÉ =================
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
    public AnnonceResponse creerAnnonce(AnnonceRequest request) {

        Utilisateur utilisateur = getUtilisateurConnecte();

        if (!RoleType.PROPRIETAIRE.name()
                .equals(utilisateur.getRole().getNom())) {
            throw new RuntimeException("Seul un propriétaire peut créer une annonce");
        }

        Bien bien = bienRepository.findById(Objects.requireNonNull(request.getIdBien()))
                .orElseThrow(() -> new RuntimeException("Bien introuvable"));

        if (!bien.getUtilisateur().getId().equals(utilisateur.getId())) {
            throw new RuntimeException("Ce bien ne vous appartient pas");
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
    public List<AnnonceResponse> listerToutesAnnonces() {

        return annonceRepository
                .findByIsDeletedFalse()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ================= GET PAR ID =================
    @Override
    public AnnonceResponse getAnnonceById(Long id) {

        Annonce annonce = annonceRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new RuntimeException("Annonce introuvable"));

        return mapToResponse(annonce);
    }

    // ================= SUPPRESSION =================
    @Override
    public void supprimerAnnonce(Long id) {

        Utilisateur utilisateur = getUtilisateurConnecte();

        Annonce annonce = annonceRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new RuntimeException("Annonce introuvable"));

        if (!annonce.getUtilisateur().getId().equals(utilisateur.getId())) {
            throw new RuntimeException("Suppression non autorisée");
        }

        annonce.setIsDeleted(true);
        annonceRepository.save(annonce);
    }
}
