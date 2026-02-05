package com.projetimmo.projet_immobilier.service.interfaces;

import com.projetimmo.projet_immobilier.dto.BienRequest;
import com.projetimmo.projet_immobilier.entity.Bien;

import java.util.List;

public interface BienService {

    Bien creerBien(BienRequest request);

    List<Bien> listerMesBiens();

    Bien modifierBien(Long idBien, BienRequest request);

    void supprimerBien(Long idBien);

    List<Bien> listerBiens();

}
