package com.projetimmo.projet_immobilier.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.projetimmo.projet_immobilier.entity.Media;
import com.projetimmo.projet_immobilier.entity.Bien;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {

    List<Media> findByBienAndIsDeletedFalse(Bien bien);

    List<Media> findByIsDeletedFalse();
}
