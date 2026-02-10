package com.projetimmo.projet_immobilier.dto;

import com.projetimmo.projet_immobilier.enums.TypeDocument;
import lombok.Data;

@Data
public class DocumentRequest {
    private TypeDocument typeDocument;
    private String urlFichier;
    private String numeroDocument;
    private String autoriteDocument;
    private Long idBien;
}
