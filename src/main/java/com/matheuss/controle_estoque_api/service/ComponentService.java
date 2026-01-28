package com.matheuss.controle_estoque_api.service;

import com.matheuss.controle_estoque_api.domain.Collaborator;
import com.matheuss.controle_estoque_api.domain.Component;
import com.matheuss.controle_estoque_api.domain.Location;
import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import com.matheuss.controle_estoque_api.domain.history.HistoryEventType;
import com.matheuss.controle_estoque_api.dto.ComponentCreateDTO;
import com.matheuss.controle_estoque_api.dto.ComponentResponseDTO;
import com.matheuss.controle_estoque_api.dto.ComponentUpdateDTO;
import com.matheuss.controle_estoque_api.mapper.ComponentMapper;
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
    private final ComponentMapper componentMapper;
    private final AssetHistoryService assetHistoryService;
    private final EntityResolver resolver;

    @Transactional
    public ComponentResponseDTO createComponent(ComponentCreateDTO dto) {
        Component entity = componentMapper.toEntity(dto);

        entity.setCategory(resolver.requireCategory(dto.getCategoryId()));
        entity.setLocation(resolver.optionalLocation(dto.getLocationId()));
        entity.setComputer(resolver.optionalComputer(dto.getComputerId()));

        entity.setStatus(AssetStatus.EM_ESTOQUE);
        entity.setCollaborator(null);

        Component saved = componentRepository.save(entity);
        assetHistoryService.registerEvent(saved, HistoryEventType.CRIACAO, "Componente cadastrado no sistema.", null);

        return componentMapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ComponentResponseDTO> getAllComponents() {
        // ATUALIZAÇÃO: Usando o método otimizado para evitar N+1 queries
        List<Component> components = componentRepository.findAllWithDetails();
        return components.stream()
                .map(componentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ComponentResponseDTO getComponentById(Long id) {
        // PADRONIZAÇÃO: Lança exceção se não encontrar
        return componentRepository.findById(id)
                .map(componentMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Componente não encontrado com o ID: " + id));
    }

    @Transactional
    public ComponentResponseDTO updateComponent(Long id, ComponentUpdateDTO dto) {
        Component entity = componentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Componente não encontrado com o ID: " + id));

        // Guarda estados antigos para comparação
        Collaborator oldCollaborator = entity.getCollaborator();
        Location oldLocation = entity.getLocation();
        AssetStatus oldStatus = entity.getStatus();

        // Aplica atualizações simples
        componentMapper.updateEntityFromDto(dto, entity);

        // Lógica de auditoria para relacionamentos e status
        Long newCollaboratorId = dto.getCollaboratorId(); // Assumindo que foi adicionado ao DTO
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
                assetHistoryService.registerEvent(entity, HistoryEventType.DEVOLUCAO, "Componente removido da localização PA " + oldLocation.getPaNumber() + " via atualização.", null);
            } else {
                Location newLocation = resolver.requireLocation(newLocationId);
                entity.setLocation(newLocation);
                assetHistoryService.registerEvent(entity, HistoryEventType.ALOCACAO, "Componente alocado para a localização PA " + newLocation.getPaNumber() + " via atualização.", null);
            }
        }

        if (dto.getStatus() != null && !Objects.equals(oldStatus, dto.getStatus())) {
            assetHistoryService.registerEvent(entity, HistoryEventType.ATUALIZACAO, "Status do componente alterado de '" + oldStatus.name() + "' para '" + dto.getStatus().name() + "'.", null);
        }

        // Resolve outras relações
        if (dto.getCategoryId() != null) entity.setCategory(resolver.requireCategory(dto.getCategoryId()));
        if (dto.getComputerId() != null) entity.setComputer(resolver.optionalComputer(dto.getComputerId()));
        
        Component updated = componentRepository.save(entity);
        return componentMapper.toResponseDTO(updated);
    }

}
