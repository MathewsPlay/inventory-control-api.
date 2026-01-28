package com.matheuss.controle_estoque_api.service.support;

import com.matheuss.controle_estoque_api.domain.Category;
import com.matheuss.controle_estoque_api.domain.Collaborator;
import com.matheuss.controle_estoque_api.domain.Computer;
import com.matheuss.controle_estoque_api.domain.Location;
import com.matheuss.controle_estoque_api.repository.CategoryRepository;
import com.matheuss.controle_estoque_api.repository.CollaboratorRepository;
import com.matheuss.controle_estoque_api.repository.ComputerRepository;
import com.matheuss.controle_estoque_api.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityResolver {

    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final ComputerRepository computerRepository;
    private final CollaboratorRepository collaboratorRepository;

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
    
    // ====================================================================
    // == MÉTODO ADICIONADO PARA CORRIGIR O ERRO ==
    // ====================================================================
    public Location requireLocation(Long id) {
        if (id == null) throw new IllegalStateException("locationId é obrigatório.");
        return locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Localização não encontrada com o ID: " + id));
    }

    public Computer optionalComputer(Long id) {
        if (id == null) return null;
        return computerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Computador não encontrado com o ID: " + id));
    }

    // ====================================================================
    // == MÉTODO CORRIGIDO E PADRONIZADO ==
    // ====================================================================
    public Collaborator requireCollaborator(Long id) {
        if (id == null) throw new IllegalStateException("collaboratorId é obrigatório.");
        return collaboratorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Colaborador não encontrado com o ID: " + id));
    }
}
