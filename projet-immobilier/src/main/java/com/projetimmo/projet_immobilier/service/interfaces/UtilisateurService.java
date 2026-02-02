package com.projetimmo.projet_immobilier.service.interfaces;

import com.projetimmo.projet_immobilier.entity.Utilisateur;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

public interface UtilisateurService {

    Utilisateur creerUtilisateur(Utilisateur utilisateur);

    Utilisateur getUtilisateurParId(@NonNull UUID id);

    List<Utilisateur> listerUtilisateurs();

    Utilisateur supprimerUtilisateur(@NonNull UUID id);
}
