package com.matheuss.controle_estoque_api.service;

import com.matheuss.controle_estoque_api.domain.Component;
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
        entity.setUser(null);

        Component saved = componentRepository.save(entity);
        assetHistoryService.registerEvent(saved, HistoryEventType.CRIACAO, "Ativo cadastrado no sistema.", null);

        return componentMapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ComponentResponseDTO> getAllComponents() {
        return componentRepository.findAll().stream()
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

        componentMapper.updateEntityFromDto(dto, entity);

        if (dto.getCategoryId() != null) entity.setCategory(resolver.requireCategory(dto.getCategoryId()));
        if (dto.getLocationId() != null) entity.setLocation(resolver.optionalLocation(dto.getLocationId()));
        if (dto.getComputerId() != null) entity.setComputer(resolver.optionalComputer(dto.getComputerId()));

        Component updated = componentRepository.save(entity);
        return componentMapper.toResponseDTO(updated);
    }

    @Transactional
    public void deleteComponent(Long id) {
        if (!componentRepository.existsById(id)) {
            throw new EntityNotFoundException("Componente não encontrado com o ID: " + id);
        }
        componentRepository.deleteById(id);
    }
}
