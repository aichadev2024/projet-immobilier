package com.projetimmo.projet_immobilier.service.interfaces;

import com.projetimmo.projet_immobilier.dto.MediaRequest;
import com.projetimmo.projet_immobilier.dto.MediaResponse;

import java.util.List;

public interface MediaService {
    MediaResponse ajouterMedia(MediaRequest request);

    List<MediaResponse> listerMediaParBien(Long idBien);

    void supprimerMedia(Long id);
}
