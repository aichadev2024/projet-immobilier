package com.projetimmo.projet_immobilier.dto;

import lombok.Data;

@Data
public class BienRequest {

    private String libelle;
    private String description;
    private Double superficie;
    private Double latitude;
    private Double longitude;
    private Double prixCalculer;
    private String adresse;
    private String statutBien;
    private Long idTypeBien;
}
