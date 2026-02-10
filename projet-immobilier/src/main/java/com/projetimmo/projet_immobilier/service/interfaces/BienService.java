package com.projetimmo.projet_immobilier.service.interfaces;

import com.projetimmo.projet_immobilier.dto.BienRequest;

import com.projetimmo.projet_immobilier.dto.BienResponse;
import java.util.List;

public interface BienService {

    BienResponse creerBien(BienRequest request);

    List<BienResponse> listerMesBiens();

    List<BienResponse> listerBiens();

    BienResponse modifierBien(Long idBien, BienRequest request);

    void supprimerBien(Long idBien);
}
