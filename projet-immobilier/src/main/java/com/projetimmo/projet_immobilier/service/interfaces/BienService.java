package com.projetimmo.projet_immobilier.service.interfaces;

import com.projetimmo.projet_immobilier.dto.BienRequest;
import com.projetimmo.projet_immobilier.entity.Bien;

import java.util.List;

public interface BienService {

    Bien creerBien(BienRequest request);

    List<Bien> listerBiens();
}
