package com.projetimmo.projet_immobilier.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.projetimmo.projet_immobilier.enums.StatutBien;

@Entity
@Table(name = "bien")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String libelle;

    private String description;

    private Double superficie;

    private Double latitude;

    private Double longitude;

    @Column(nullable = false)
    private Double prixCalculer;

    @Column(nullable = false)
    private String adresse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutBien statutBien;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isDeleted = false;

    private LocalDateTime deletedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // 🔗 PROPRIÉTAIRE
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private Utilisateur utilisateur;

    // 🔗 TYPE DE BIEN
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_bien", nullable = false)
    private TypeBien typeBien;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
