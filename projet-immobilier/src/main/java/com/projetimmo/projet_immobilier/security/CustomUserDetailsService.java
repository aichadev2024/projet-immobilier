package com.projetimmo.projet_immobilier.security;

import com.projetimmo.projet_immobilier.entity.Utilisateur;
import com.projetimmo.projet_immobilier.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String nomUtilisateur)
            throws UsernameNotFoundException {

        Utilisateur utilisateur = utilisateurRepository
                .findByNomUtilisateur(nomUtilisateur)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

        return new User(
                utilisateur.getNomUtilisateur(),
                utilisateur.getMotDePasse(),
                List.of(new SimpleGrantedAuthority(
                        "ROLE_" + utilisateur.getRole().getNom())));
    }
}
