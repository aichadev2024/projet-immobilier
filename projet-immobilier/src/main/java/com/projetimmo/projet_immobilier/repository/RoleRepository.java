package com.projetimmo.projet_immobilier.repository;

import com.projetimmo.projet_immobilier.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByNom(String nom);

    boolean existsByNom(String nom);
}
