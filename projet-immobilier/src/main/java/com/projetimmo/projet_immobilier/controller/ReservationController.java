package com.projetimmo.projet_immobilier.controller;

import com.projetimmo.projet_immobilier.dto.ReservationRequest;
import com.projetimmo.projet_immobilier.dto.ReservationResponse;
import com.projetimmo.projet_immobilier.service.interfaces.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ReservationResponse creer(@RequestBody ReservationRequest request) {
        return reservationService.creerReservation(request);
    }

    @GetMapping("/bien/{idBien}")
    public List<ReservationResponse> reservationsParBien(@PathVariable Long idBien) {
        return reservationService.listerReservationsParBien(idBien);
    }

    @DeleteMapping("/{id}")
    public void annuler(@PathVariable Long id) {
        reservationService.annulerReservation(id);
    }
}
