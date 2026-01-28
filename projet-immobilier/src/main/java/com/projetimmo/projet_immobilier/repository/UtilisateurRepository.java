package com.projetimmo.projet_immobilier.repository;

import com.projetimmo.projet_immobilier.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, UUID> {

    Optional<Utilisateur> findByEmail(String email);

    Optional<Utilisateur> findByNomUtilisateur(String nomUtilisateur);

    boolean existsByEmail(String email);

    boolean existsByNomUtilisateur(String nomUtilisateur);
}
