package com.matheuss.controle_estoque_api.service.support;

// ====================================================================
// == IMPORTS CORRIGIDOS PARA EVITAR COLISÃO ==
// ====================================================================
import com.matheuss.controle_estoque_api.domain.Category;
import com.matheuss.controle_estoque_api.domain.Collaborator;
import com.matheuss.controle_estoque_api.domain.Computer;
import com.matheuss.controle_estoque_api.domain.Location;
import com.matheuss.controle_estoque_api.domain.Component; // 1. Mantemos o import da nossa entidade.
import com.matheuss.controle_estoque_api.repository.CategoryRepository;
import com.matheuss.controle_estoque_api.repository.CollaboratorRepository;
import com.matheuss.controle_estoque_api.repository.ComponentRepository;
import com.matheuss.controle_estoque_api.repository.ComputerRepository;
import com.matheuss.controle_estoque_api.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
// 2. REMOVEMOS o import 'org.springframework.stereotype.Component' para evitar o conflito.

// 3. Usamos o "nome completo" da anotação aqui para que o Java saiba exatamente qual é.
@org.springframework.stereotype.Component 
@RequiredArgsConstructor
public class EntityResolver {

    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final ComputerRepository computerRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final ComponentRepository componentRepository;

    // ... (todos os seus outros métodos permanecem exatamente iguais) ...
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

    public Computer requireComputer(Long id) {
        if (id == null) throw new IllegalStateException("O ID do computador é obrigatório.");
        return computerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Computador não encontrado com o ID: " + id));
    }

    public Collaborator requireCollaborator(Long id) {
        if (id == null) throw new IllegalStateException("collaboratorId é obrigatório.");
        return collaboratorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Colaborador não encontrado com o ID: " + id));
    }
    
    public Component requireComponent(Long id) {
        if (id == null) throw new IllegalStateException("O ID do componente é obrigatório.");
        return componentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Componente não encontrado com o ID: " + id));
    }
    public Collaborator optionalCollaborator(Long id) {
    if (id == null) return null;
    return collaboratorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Colaborador não encontrado com o ID: " + id));
}
}
