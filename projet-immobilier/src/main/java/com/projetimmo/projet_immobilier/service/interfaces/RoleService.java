package com.projetimmo.projet_immobilier.service.interfaces;

import com.projetimmo.projet_immobilier.entity.Role;
import com.projetimmo.projet_immobilier.enums.RoleType;

public interface RoleService {

    Role trouverParNom(RoleType nom);

    Role getRoleParDefaut();
}
