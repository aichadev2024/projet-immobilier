package com.projetimmo.projet_immobilier.dto;

import com.projetimmo.projet_immobilier.enums.StatutBien;
import lombok.Data;

@Data
public class BienRequest {

    private String libelle;
    private String description;
    private Double superficie;
    private Double latitude;
    private Double longitude;

    private String adresse;

    private StatutBien statutBien; // ✅ ICI

    private Long idTypeBien;
}
