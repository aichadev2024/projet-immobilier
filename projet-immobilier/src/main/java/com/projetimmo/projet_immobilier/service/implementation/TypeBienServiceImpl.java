package com.projetimmo.projet_immobilier.service.implementation;

import com.projetimmo.projet_immobilier.entity.TypeBien;
import com.projetimmo.projet_immobilier.repository.TypeBienRepository;
import com.projetimmo.projet_immobilier.service.interfaces.TypeBienService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeBienServiceImpl implements TypeBienService {

    private final TypeBienRepository repository;

    @Override
    public TypeBien create(TypeBien typeBien) {

        if (typeBien == null) {
            throw new IllegalArgumentException("TypeBien ne peut pas être null");
        }

        if (repository.findByLibelleAndIsDeletedFalse(typeBien.getLibelle()).isPresent()) {
            throw new RuntimeException("Type de bien déjà existant");
        }

        return repository.save(typeBien);
    }

    @Override
    public List<TypeBien> findAll() {
        return repository.findAll()
                .stream()
                .filter(tb -> !tb.getIsDeleted())
                .toList();
    }

    @Override
    public TypeBien findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id ne peut pas être null");
        }

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Type de bien introuvable"));
    }

    @Override
    public TypeBien update(Long id, TypeBien typeBien) {
        TypeBien existing = findById(id);
        existing.setLibelle(typeBien.getLibelle());
        existing.setDescription(typeBien.getDescription());
        existing.setModeTarification(typeBien.getModeTarification());
        return repository.save(existing);
    }

    @Override
    public void delete(Long id) {
        TypeBien typeBien = findById(id);
        typeBien.setIsDeleted(true);
        repository.save(typeBien);
    }
}
