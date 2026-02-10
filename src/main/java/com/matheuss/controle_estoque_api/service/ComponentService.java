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
import com.matheuss.controle_estoque_api.repository.AssetRepository;
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
    private final AssetRepository assetRepository;
    private final ComponentMapper componentMapper;
    private final AssetHistoryService assetHistoryService;
    private final EntityResolver resolver;

    @Transactional
    public ComponentResponseDTO createComponent(ComponentCreateDTO dto) {
        // LÓGICA EXISTENTE PRESERVADA
        if (dto.getPatrimonio() != null && !dto.getPatrimonio().isBlank() && assetRepository.existsByPatrimonio(dto.getPatrimonio())) {
            throw new IllegalStateException("Já existe um ativo com o número de patrimônio: " + dto.getPatrimonio());
        }
        if (dto.getAssetTag() != null && !dto.getAssetTag().isBlank() && assetRepository.existsByAssetTag(dto.getAssetTag())) {
            throw new IllegalStateException("Já existe um ativo com o Asset Tag: " + dto.getAssetTag());
        }

        Component entity = componentMapper.toEntity(dto);
        entity.setCategory(resolver.requireCategory(dto.getCategoryId()));
        entity.setLocation(resolver.optionalLocation(dto.getLocationId()));
        entity.setComputer(null);
        entity.setStatus(AssetStatus.EM_ESTOQUE);
        entity.setCollaborator(null);
        Component saved = componentRepository.save(entity);
        assetHistoryService.registerEvent(saved, HistoryEventType.CRIACAO, "Componente cadastrado no sistema.", null);
        return componentMapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ComponentResponseDTO> getAllComponents() {
        // LÓGICA EXISTENTE PRESERVADA
        List<Component> components = componentRepository.findAllWithDetails();
        return components.stream()
                .map(componentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ComponentResponseDTO getComponentById(Long id) {
        // LÓGICA EXISTENTE PRESERVADA
        return componentRepository.findById(id)
                .map(componentMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Componente não encontrado com o ID: " + id));
    }

    @Transactional
    public ComponentResponseDTO updateComponent(Long id, ComponentUpdateDTO dto) {
        // 1. BUSCA DE ENTIDADES
        Component component = resolver.requireComponent(id);
        Location newLocation = resolver.optionalLocation(dto.getLocationId());
        Collaborator newCollaborator = resolver.optionalCollaborator(dto.getCollaboratorId());

        // 2. VALIDAÇÃO DE UNICIDADE (Sua lógica, preservada)
        if (dto.getPatrimonio() != null && !dto.getPatrimonio().isBlank() && !Objects.equals(component.getPatrimonio(), dto.getPatrimonio())) {
            if (assetRepository.existsByPatrimonio(dto.getPatrimonio())) {
                throw new IllegalStateException("Operação não permitida: Já existe outro ativo com o patrimônio: " + dto.getPatrimonio());
            }
        }
        if (dto.getAssetTag() != null && !dto.getAssetTag().isBlank() && !Objects.equals(component.getAssetTag(), dto.getAssetTag())) {
            if (assetRepository.existsByAssetTag(dto.getAssetTag())) {
                throw new IllegalStateException("Operação não permitida: Já existe outro ativo com o Asset Tag: " + dto.getAssetTag());
            }
        }

        // 3. ATUALIZAÇÃO DOS CAMPOS SIMPLES
        // O Mapper NÃO deve mais atualizar location, collaborator e status.
        componentMapper.updateEntityFromDto(dto, component);
        if (dto.getCategoryId() != null) {
            component.setCategory(resolver.requireCategory(dto.getCategoryId()));
        }

        // 4. LÓGICA DE ESTADO E ALOCAÇÃO
        Location oldLocation = component.getLocation();
        Collaborator oldCollaborator = component.getCollaborator();

        if ((newLocation != null || newCollaborator != null) && component.getComputer() != null) {
            throw new IllegalStateException("Operação não permitida: Um componente instalado em um computador não pode ser alocado para uma pessoa ou localização.");
        }
        if (newLocation != null && newCollaborator != null) {
            throw new IllegalStateException("Operação não permitida: Um ativo não pode ser alocado para um colaborador e uma localização ao mesmo tempo.");
        }

        component.setLocation(newLocation);
        component.setCollaborator(newCollaborator);

        if (newLocation != null || newCollaborator != null || component.getComputer() != null) {
            component.setStatus(AssetStatus.EM_USO);
        } else {
            component.setStatus(AssetStatus.EM_ESTOQUE);
        }

        // 5. REGISTRO DE HISTÓRICO INTELIGENTE
        assetHistoryService.registerEvent(component, HistoryEventType.ATUALIZACAO, "Dados do ativo foram atualizados.", null);

        if (!Objects.equals(oldLocation, newLocation)) {
            if (newLocation != null) {
               assetHistoryService.registerEvent(component, HistoryEventType.ALOCACAO, "Ativo alocado para a localização PA: " + newLocation.getPaNumber(), null);
            }
            if (oldLocation != null) {
                assetHistoryService.registerEvent(component, HistoryEventType.DEVOLUCAO, "Ativo devolvido da localização PA: " + oldLocation.getPaNumber(), null);
            }
        }
        if (!Objects.equals(oldCollaborator, newCollaborator)) {
            if (newCollaborator != null) {
                assetHistoryService.registerEvent(component, HistoryEventType.ALOCACAO, "Ativo alocado para o colaborador: " + newCollaborator.getName(), null);
            }
            if (oldCollaborator != null) {
                assetHistoryService.registerEvent(component, HistoryEventType.DEVOLUCAO, "Ativo devolvido pelo colaborador: " + oldCollaborator.getName(), null);
            }
        }

        // 6. SALVAR E RETORNAR
        Component updatedComponent = componentRepository.save(component);
        return componentMapper.toResponseDTO(updatedComponent);
    }

    @Transactional
    public ComponentResponseDTO installComponent(Long componentId, Long computerId) {
        // LÓGICA EXISTENTE PRESERVADA
        Component component = resolver.requireComponent(componentId);
        Computer computer = resolver.requireComputer(computerId);

        if (component.getComputer() != null) {
            throw new IllegalStateException("Operação não permitida: O componente já está instalado no computador com ID " + component.getComputer().getId());
        }
        if (component.getStatus() != AssetStatus.EM_ESTOQUE) {
            throw new IllegalStateException("Operação não permitida: Apenas componentes 'EM ESTOQUE' podem ser instalados.");
        }

        component.setComputer(computer);
        component.setStatus(AssetStatus.EM_USO);
        component.setLocation(null);
        component.setCollaborator(null);

        Component updatedComponent = componentRepository.save(component);

        String details = String.format("Componente '%s' (Patrimônio: %s) instalado no computador '%s'.",
                updatedComponent.getName(), updatedComponent.getPatrimonio(), computer.getName());
        assetHistoryService.registerEvent(updatedComponent, HistoryEventType.INSTALACAO, details, null);

        return componentMapper.toResponseDTO(updatedComponent);
    }
}
