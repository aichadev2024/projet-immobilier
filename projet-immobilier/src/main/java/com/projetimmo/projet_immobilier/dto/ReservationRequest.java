package com.projetimmo.projet_immobilier.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ReservationRequest {
    private Long idBien;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
}
