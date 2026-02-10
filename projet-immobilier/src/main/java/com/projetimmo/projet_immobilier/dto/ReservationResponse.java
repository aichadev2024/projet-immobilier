package com.projetimmo.projet_immobilier.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ReservationResponse {
    private Long id;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String statut;
    private LocalDateTime dateReservation;
    private Long idBien;
    private String libelleBien;
    private UUID idClient; // ⚠ UUID pour correspondre à Utilisateur.id
    private String nomClient;
}
