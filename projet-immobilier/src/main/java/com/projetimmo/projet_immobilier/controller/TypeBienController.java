package com.projetimmo.projet_immobilier.controller;

import com.projetimmo.projet_immobilier.entity.TypeBien;
import com.projetimmo.projet_immobilier.service.interfaces.TypeBienService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/type-biens")
@RequiredArgsConstructor
public class TypeBienController {

    private final TypeBienService service;

    @PostMapping
    public TypeBien create(@RequestBody TypeBien typeBien) {
        return service.create(typeBien);
    }

    @GetMapping
    public List<TypeBien> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public TypeBien getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public TypeBien update(@PathVariable Long id, @RequestBody TypeBien typeBien) {
        return service.update(id, typeBien);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
