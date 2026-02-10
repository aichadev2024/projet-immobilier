package com.projetimmo.projet_immobilier.repository;

import com.projetimmo.projet_immobilier.entity.Annonce;
import com.projetimmo.projet_immobilier.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnonceRepository extends JpaRepository<Annonce, Long> {

    List<Annonce> findByUtilisateurAndIsDeletedFalse(Utilisateur utilisateur);

    List<Annonce> findByIsDeletedFalse();
}
