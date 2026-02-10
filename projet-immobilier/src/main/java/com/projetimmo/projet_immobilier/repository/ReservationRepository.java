package com.projetimmo.projet_immobilier.repository;

import com.projetimmo.projet_immobilier.entity.Reservation;
import com.projetimmo.projet_immobilier.entity.Bien;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByBienAndIsDeletedFalse(Bien bien);
}
