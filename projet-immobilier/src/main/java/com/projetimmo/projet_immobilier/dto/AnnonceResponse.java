package com.projetimmo.projet_immobilier.dto;

import com.projetimmo.projet_immobilier.enums.TypeAnnonce;
import com.projetimmo.projet_immobilier.enums.StatutAnnonce;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnnonceResponse {

    private Long id;
    private TypeAnnonce typeAnnonce;
    private StatutAnnonce statut;
    private Long idBien;
    private String libelleBien;
}
