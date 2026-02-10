package com.projetimmo.projet_immobilier.service.implementation;

import com.projetimmo.projet_immobilier.dto.ReservationRequest;
import com.projetimmo.projet_immobilier.dto.ReservationResponse;
import com.projetimmo.projet_immobilier.entity.Bien;
import com.projetimmo.projet_immobilier.entity.Reservation;
import com.projetimmo.projet_immobilier.enums.StatutReservation;
import com.projetimmo.projet_immobilier.entity.Utilisateur;
import com.projetimmo.projet_immobilier.repository.BienRepository;
import com.projetimmo.projet_immobilier.repository.ReservationRepository;
import com.projetimmo.projet_immobilier.repository.UtilisateurRepository;
import com.projetimmo.projet_immobilier.service.interfaces.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final BienRepository bienRepository;
    private final UtilisateurRepository utilisateurRepository;

    // 🔹 Récupérer l'utilisateur connecté
    private Utilisateur getUtilisateurConnecte() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return utilisateurRepository
                .findByNomUtilisateur(auth.getName())
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable"));
    }

    // 🔹 Mapper Entité → DTO
    private ReservationResponse mapToResponse(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .dateDebut(reservation.getDateDebut())
                .dateFin(reservation.getDateFin())
                .statut(reservation.getStatut().name()) // Enum → String
                .dateReservation(reservation.getDateReservation())
                .idBien(reservation.getBien().getId())
                .libelleBien(reservation.getBien().getLibelle())
                .idClient(reservation.getUtilisateur().getId()) // UUID
                .nomClient(reservation.getUtilisateur().getNomUtilisateur())
                .build();
    }

    // 🔹 Créer une réservation
    @Override
    @PreAuthorize("hasRole('CLIENT')")
    public ReservationResponse creerReservation(ReservationRequest request) {
        Utilisateur client = getUtilisateurConnecte();

        Bien bien = bienRepository.findById(Objects.requireNonNull(request.getIdBien()))
                .orElseThrow(() -> new IllegalArgumentException("Bien introuvable"));

        Reservation reservation = Reservation.builder()
                .dateDebut(request.getDateDebut())
                .dateFin(request.getDateFin())
                .statut(StatutReservation.EN_ATTENTE) // Enum
                .dateReservation(LocalDateTime.now())
                .utilisateur(client)
                .bien(bien)
                .build();

        Reservation saved = reservationRepository.save(Objects.requireNonNull(reservation));
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> listerReservationsParBien(Long idBien) {
        Bien bien = bienRepository.findById(Objects.requireNonNull(idBien))
                .orElseThrow(() -> new IllegalArgumentException("Bien introuvable"));

        return reservationRepository.findByBienAndIsDeletedFalse(bien)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 🔹 Annuler une réservation
    @Override
    @PreAuthorize("hasRole('CLIENT')")
    public void annulerReservation(Long id) {
        Utilisateur client = getUtilisateurConnecte();

        Reservation reservation = reservationRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new IllegalArgumentException("Réservation introuvable"));

        if (!reservation.getUtilisateur().getId().equals(client.getId())) {
            throw new SecurityException("Annulation non autorisée");
        }

        reservation.setIsDeleted(true);
        reservation.setDeletedAt(LocalDateTime.now());
        reservationRepository.save(reservation);
    }
}
