package com.projetimmo.projet_immobilier.repository;

import com.projetimmo.projet_immobilier.entity.Bien;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BienRepository extends JpaRepository<Bien, Long> {

    List<Bien> findByIsDeletedFalse();

    List<Bien> findByUtilisateurIdAndIsDeletedFalse(Long utilisateurId);
}
