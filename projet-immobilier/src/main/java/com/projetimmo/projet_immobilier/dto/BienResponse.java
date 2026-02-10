package com.projetimmo.projet_immobilier.dto;

import com.projetimmo.projet_immobilier.enums.StatutBien;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BienResponse {

    private Long id;
    private String libelle;
    private String description;

    private Double superficie;
    private Double latitude;
    private Double longitude;
    private String adresse;

    private Double prixCalcule;
    private StatutBien statutBien;

    // Infos TypeBien (sans relation lourde)
    private Long idTypeBien;
    private String libelleTypeBien;
}
