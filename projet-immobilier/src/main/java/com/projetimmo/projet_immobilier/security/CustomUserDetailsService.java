package com.projetimmo.projet_immobilier.security;

import com.projetimmo.projet_immobilier.entity.Utilisateur;
import com.projetimmo.projet_immobilier.enums.StatutUtilisateur;
import com.projetimmo.projet_immobilier.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

        private final UtilisateurRepository utilisateurRepository;

        @Override
        public UserDetails loadUserByUsername(String nomUtilisateur)
                        throws UsernameNotFoundException {

                System.out.println("✅ LOGIN REÇU = [" + nomUtilisateur + "]");

                Utilisateur utilisateur = utilisateurRepository
                                .findByNomUtilisateur(nomUtilisateur)
                                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

                System.out.println("✅ USER TROUVÉ = " + utilisateur.getNomUtilisateur());
                System.out.println("✅ MOT DE PASSE (DB) = " + utilisateur.getMotDePasse());
                return org.springframework.security.core.userdetails.User.builder()
                                .username(utilisateur.getNomUtilisateur())
                                .password(utilisateur.getMotDePasse())
                                .authorities("ROLE_" + utilisateur.getRole().getNom())
                                .disabled(utilisateur.getStatut() != StatutUtilisateur.ACTIF)
                                .accountExpired(false)
                                .accountLocked(false)
                                .credentialsExpired(false)
                                .build();

        }
}
