package com.matheuss.controle_estoque_api.service;

import com.matheuss.controle_estoque_api.domain.Collaborator;
import com.matheuss.controle_estoque_api.domain.Component;
import com.matheuss.controle_estoque_api.domain.Computer;
import com.matheuss.controle_estoque_api.domain.Location;
import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import com.matheuss.controle_estoque_api.domain.history.HistoryEventType;
import com.matheuss.controle_estoque_api.dto.ComponentCreateDTO;
import com.matheuss.controle_estoque_api.dto.ComponentResponseDTO;
import com.matheuss.controle_estoque_api.dto.ComponentUpdateDTO;
import com.matheuss.controle_estoque_api.mapper.ComponentMapper;
import com.matheuss.controle_estoque_api.repository.AssetRepository; // 1. IMPORTAR
import com.matheuss.controle_estoque_api.repository.ComponentRepository;
import com.matheuss.controle_estoque_api.service.support.EntityResolver;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComponentService {

    private final ComponentRepository componentRepository;
    private final AssetRepository assetRepository; // 2. ADICIONAR INJEÇÃO
    private final ComponentMapper componentMapper;
    private final AssetHistoryService assetHistoryService;
    private final EntityResolver resolver;

    @Transactional
    public ComponentResponseDTO createComponent(ComponentCreateDTO dto) {
        // ====================================================================
        // == LÓGICA DE VALIDAÇÃO DE UNICIDADE DOS IDENTIFICADORES ==
        // ====================================================================
        if (dto.getPatrimonio() != null && !dto.getPatrimonio().isBlank() && assetRepository.existsByPatrimonio(dto.getPatrimonio())) {
            throw new IllegalStateException("Já existe um ativo com o número de patrimônio: " + dto.getPatrimonio());
        }
        if (dto.getAssetTag() != null && !dto.getAssetTag().isBlank() && assetRepository.existsByAssetTag(dto.getAssetTag())) {
            throw new IllegalStateException("Já existe um ativo com o Asset Tag: " + dto.getAssetTag());
        }

        // Lógica de criação original, agora sem a associação com o computador
        Component entity = componentMapper.toEntity(dto);

        entity.setCategory(resolver.requireCategory(dto.getCategoryId()));
        entity.setLocation(resolver.optionalLocation(dto.getLocationId()));
        entity.setComputer(null); // Garante que um componente novo nunca está instalado

        entity.setStatus(AssetStatus.EM_ESTOQUE);
        entity.setCollaborator(null);

        Component saved = componentRepository.save(entity);
        assetHistoryService.registerEvent(saved, HistoryEventType.CRIACAO, "Componente cadastrado no sistema.", null);

        return componentMapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ComponentResponseDTO> getAllComponents() {
        List<Component> components = componentRepository.findAllWithDetails();
        return components.stream()
                .map(componentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ComponentResponseDTO getComponentById(Long id) {
        return componentRepository.findById(id)
                .map(componentMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Componente não encontrado com o ID: " + id));
    }

    @Transactional
    public ComponentResponseDTO updateComponent(Long id, ComponentUpdateDTO dto) {
        Component entity = componentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Componente não encontrado com o ID: " + id));

        // ====================================================================
        // == VALIDAÇÃO DE UNICIDADE ADICIONADA AO UPDATE ==
        // ====================================================================
        if (dto.getPatrimonio() != null && !dto.getPatrimonio().isBlank() && !Objects.equals(entity.getPatrimonio(), dto.getPatrimonio())) {
            if (assetRepository.existsByPatrimonio(dto.getPatrimonio())) {
                throw new IllegalStateException("Já existe outro ativo com o número de patrimônio: " + dto.getPatrimonio());
            }
        }
        if (dto.getAssetTag() != null && !dto.getAssetTag().isBlank() && !Objects.equals(entity.getAssetTag(), dto.getAssetTag())) {
            if (assetRepository.existsByAssetTag(dto.getAssetTag())) {
                throw new IllegalStateException("Já existe outro ativo com o Asset Tag: " + dto.getAssetTag());
            }
        }

        // Lógica de "PUT Inteligente" original preservada
        Collaborator oldCollaborator = entity.getCollaborator();
        Location oldLocation = entity.getLocation();
        AssetStatus oldStatus = entity.getStatus();

        componentMapper.updateEntityFromDto(dto, entity);

        Long newCollaboratorId = dto.getCollaboratorId();
        Long oldCollaboratorId = (oldCollaborator != null) ? oldCollaborator.getId() : null;
        if (!Objects.equals(oldCollaboratorId, newCollaboratorId)) {
            if (newCollaboratorId == null) {
                entity.setCollaborator(null);
                assetHistoryService.registerEvent(entity, HistoryEventType.DEVOLUCAO, "Componente desvinculado do colaborador via atualização.", oldCollaborator);
            } else {
                Collaborator newCollaborator = resolver.requireCollaborator(newCollaboratorId);
                entity.setCollaborator(newCollaborator);
                assetHistoryService.registerEvent(entity, HistoryEventType.ALOCACAO, "Componente alocado para o colaborador " + newCollaborator.getName() + " via atualização.", newCollaborator);
            }
        }
        
        Long newLocationId = dto.getLocationId();
        Long oldLocationId = (oldLocation != null) ? oldLocation.getId() : null;
        if (!Objects.equals(oldLocationId, newLocationId)) {
             if (newLocationId == null) {
                entity.setLocation(null);
                String details = "Componente desvinculado de sua localização anterior via atualização.";
                if (oldLocation != null) {
                    details = "Componente removido da localização PA " + oldLocation.getPaNumber() + " via atualização.";
                }
                assetHistoryService.registerEvent(entity, HistoryEventType.DEVOLUCAO, details, null);
            } else {
                Location newLocation = resolver.requireLocation(newLocationId);
                entity.setLocation(newLocation);
                assetHistoryService.registerEvent(entity, HistoryEventType.ALOCACAO, "Componente alocado para a localização PA " + newLocation.getPaNumber() + " via atualização.", null);
            }
        }

        if (dto.getStatus() != null && !Objects.equals(oldStatus, dto.getStatus())) {
            assetHistoryService.registerEvent(entity, HistoryEventType.ATUALIZACAO, "Status do componente alterado de '" + oldStatus.name() + "' para '" + dto.getStatus().name() + "'.", null);
        }

        if (dto.getCategoryId() != null) entity.setCategory(resolver.requireCategory(dto.getCategoryId()));
        if (dto.getComputerId() != null) entity.setComputer(resolver.optionalComputer(dto.getComputerId()));
        
        Component updated = componentRepository.save(entity);
        return componentMapper.toResponseDTO(updated);
    }

    @Transactional
public ComponentResponseDTO installComponent(Long componentId, Long computerId) {
    // 1. Usar o resolver para buscar as entidades (padrão do projeto)
    Component component = resolver.requireComponent(componentId);
    Computer computer = resolver.requireComputer(computerId);

    // 2. Validações de negócio
    if (component.getComputer() != null) {
        throw new IllegalStateException("Operação não permitida: O componente já está instalado no computador com ID " + component.getComputer().getId());
    }
    if (component.getStatus() != AssetStatus.EM_ESTOQUE) {
        throw new IllegalStateException("Operação não permitida: Apenas componentes 'EM ESTOQUE' podem ser instalados.");
    }

    // 3. Atualizar o estado do componente
    component.setComputer(computer);
    component.setStatus(AssetStatus.EM_USO);
    component.setLocation(null);      // Garante que não está associado a um local físico
    component.setCollaborator(null);  // Garante que não está associado a um colaborador

    // 4. Salvar e capturar a entidade atualizada
    Component updatedComponent = componentRepository.save(component);

    // 5. Registrar o evento de histórico com detalhes
    String details = String.format("Componente '%s' (Patrimônio: %s) instalado no computador '%s'.",
            updatedComponent.getName(), updatedComponent.getPatrimonio(), computer.getName());
    assetHistoryService.registerEvent(updatedComponent, HistoryEventType.INSTALACAO, details, null);

    
    // 6. Retornar o DTO, como o Controller espera
    return componentMapper.toResponseDTO(updatedComponent);
}
}
