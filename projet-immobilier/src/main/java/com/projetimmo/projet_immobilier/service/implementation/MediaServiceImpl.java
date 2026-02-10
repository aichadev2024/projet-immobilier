package com.projetimmo.projet_immobilier.service.implementation;

import com.projetimmo.projet_immobilier.dto.MediaRequest;
import com.projetimmo.projet_immobilier.dto.MediaResponse;
import com.projetimmo.projet_immobilier.entity.Bien;
import com.projetimmo.projet_immobilier.entity.Media;
import com.projetimmo.projet_immobilier.entity.Utilisateur;
import com.projetimmo.projet_immobilier.repository.BienRepository;
import com.projetimmo.projet_immobilier.repository.MediaRepository;
import com.projetimmo.projet_immobilier.repository.UtilisateurRepository;
import com.projetimmo.projet_immobilier.service.interfaces.MediaService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final BienRepository bienRepository;
    private final UtilisateurRepository utilisateurRepository;

    private Utilisateur getUtilisateurConnecte() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return utilisateurRepository
                .findByNomUtilisateur(auth.getName())
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable"));
    }

    private MediaResponse mapToResponse(Media media) {
        return MediaResponse.builder()
                .id(media.getId())
                .typeMedia(media.getTypeMedia())
                .url(media.getUrl())
                .idBien(media.getBien().getId())
                .libelleBien(media.getBien().getLibelle())
                .build();
    }

    // ================= AJOUT MEDIA =================
    @Override
    @PreAuthorize("hasRole('PROPRIETAIRE')")
    public MediaResponse ajouterMedia(MediaRequest request) {

        Utilisateur utilisateur = getUtilisateurConnecte();

        Bien bien = bienRepository.findById(Objects.requireNonNull(request.getIdBien()))
                .orElseThrow(() -> new IllegalArgumentException("Bien introuvable"));

        if (!bien.getUtilisateur().getId().equals(utilisateur.getId())) {
            throw new SecurityException("Ce bien ne vous appartient pas");
        }

        Media media = Media.builder()
                .typeMedia(request.getTypeMedia())
                .url(request.getUrl())
                .bien(bien)
                .build();

        return mapToResponse(mediaRepository.save(Objects.requireNonNull(media)));
    }

    // ================= MEDIA PAR BIEN =================
    @Override
    @Transactional(readOnly = true)
    public List<MediaResponse> listerMediaParBien(Long idBien) {
        Bien bien = bienRepository.findById(Objects.requireNonNull(idBien))
                .orElseThrow(() -> new IllegalArgumentException("Bien introuvable"));

        return mediaRepository.findByBienAndIsDeletedFalse(bien)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ================= SUPPRESSION =================
    @Override
    @PreAuthorize("hasRole('PROPRIETAIRE')")
    public void supprimerMedia(Long id) {

        Utilisateur utilisateur = getUtilisateurConnecte();

        Media media = mediaRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new IllegalArgumentException("Media introuvable"));

        if (!media.getBien().getUtilisateur().getId().equals(utilisateur.getId())) {
            throw new SecurityException("Suppression non autorisée");
        }

        media.setIsDeleted(true);
        mediaRepository.save(media);
    }
}
