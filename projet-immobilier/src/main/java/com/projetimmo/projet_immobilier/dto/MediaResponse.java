package com.projetimmo.projet_immobilier.dto;

import lombok.Getter;
import lombok.Builder;
import com.projetimmo.projet_immobilier.enums.TypeMedia;

@Getter
@Builder
public class MediaResponse {
    private Long id;
    private TypeMedia typeMedia;
    private String url;
    private Long idBien;
    private String libelleBien;
}
