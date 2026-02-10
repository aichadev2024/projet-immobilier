package com.projetimmo.projet_immobilier.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.projetimmo.projet_immobilier.service.interfaces.MediaService;
import com.projetimmo.projet_immobilier.dto.MediaRequest;
import com.projetimmo.projet_immobilier.dto.MediaResponse;

import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/medias")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping
    public MediaResponse ajouter(@RequestBody MediaRequest request) {
        return mediaService.ajouterMedia(request);
    }

    @GetMapping("/bien/{idBien}")
    public List<MediaResponse> mediasParBien(@PathVariable Long idBien) {
        return mediaService.listerMediaParBien(idBien);
    }

    @DeleteMapping("/{id}")
    public void supprimer(@PathVariable Long id) {
        mediaService.supprimerMedia(id);
    }
}
