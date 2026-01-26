package com.matheuss.controle_estoque_api.service.support;

import com.matheuss.controle_estoque_api.domain.Category;
import com.matheuss.controle_estoque_api.domain.Computer;
import com.matheuss.controle_estoque_api.domain.Location;
import com.matheuss.controle_estoque_api.domain.Collaborator;
import com.matheuss.controle_estoque_api.repository.CategoryRepository;
import com.matheuss.controle_estoque_api.repository.ComputerRepository;
import com.matheuss.controle_estoque_api.repository.LocationRepository;
import com.matheuss.controle_estoque_api.repository.CollaboratorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityResolver {

    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final ComputerRepository computerRepository;
    private final CollaboratorRepository userRepository;

    public Category requireCategory(Long id) {
        if (id == null) throw new IllegalStateException("categoryId é obrigatório.");
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com o ID: " + id));
    }

    public Location optionalLocation(Long id) {
        if (id == null) return null;
        return locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Localização não encontrada com o ID: " + id));
    }

    public Computer optionalComputer(Long id) {
        if (id == null) return null;
        return computerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Computador não encontrado com o ID: " + id));
    }

    public Collaborator requireUser(Long id) {
        if (id == null) throw new IllegalStateException("userId é obrigatório.");
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Colaborador não encontrado com o ID: " + id));
    }
}
