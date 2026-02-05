package com.matheuss.controle_estoque_api.service;

import com.matheuss.controle_estoque_api.domain.Collaborator;
import com.matheuss.controle_estoque_api.domain.Location;
import com.matheuss.controle_estoque_api.domain.Peripheral;
import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import com.matheuss.controle_estoque_api.domain.history.HistoryEventType;
import com.matheuss.controle_estoque_api.dto.PeripheralCreateDTO;
import com.matheuss.controle_estoque_api.dto.PeripheralResponseDTO;
import com.matheuss.controle_estoque_api.dto.PeripheralUpdateDTO;
import com.matheuss.controle_estoque_api.mapper.PeripheralMapper;
import com.matheuss.controle_estoque_api.repository.AssetRepository; // 1. IMPORTAR
import com.matheuss.controle_estoque_api.repository.PeripheralRepository;
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
public class PeripheralService {

    private final PeripheralRepository peripheralRepository;
    private final AssetRepository assetRepository; // 2. ADICIONAR INJEÇÃO
    private final PeripheralMapper peripheralMapper;
    private final AssetHistoryService assetHistoryService;
    private final EntityResolver resolver;

    @Transactional
    public PeripheralResponseDTO createPeripheral(PeripheralCreateDTO dto) {
        // ====================================================================
        // == LÓGICA DE VALIDAÇÃO DE UNICIDADE DOS IDENTIFICADORES ==
        // ====================================================================
        if (dto.getPatrimonio() != null && !dto.getPatrimonio().isBlank() && assetRepository.existsByPatrimonio(dto.getPatrimonio())) {
            throw new IllegalStateException("Já existe um ativo com o número de patrimônio: " + dto.getPatrimonio());
        }
        if (dto.getAssetTag() != null && !dto.getAssetTag().isBlank() && assetRepository.existsByAssetTag(dto.getAssetTag())) {
            throw new IllegalStateException("Já existe um ativo com o Asset Tag: " + dto.getAssetTag());
        }
        // A validação de Serial Number pode ser adicionada aqui se necessário.

        // Lógica de criação original preservada
        Peripheral entity = peripheralMapper.toEntity(dto);

        entity.setLocation(resolver.optionalLocation(dto.getLocationId()));
        entity.setComputer(resolver.optionalComputer(dto.getComputerId()));
        entity.setStatus(AssetStatus.EM_ESTOQUE);
        entity.setCollaborator(null);

        Peripheral saved = peripheralRepository.save(entity);
        assetHistoryService.registerEvent(saved, HistoryEventType.CRIACAO, "Periférico cadastrado no sistema.", null);

        return peripheralMapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<PeripheralResponseDTO> getAllPeripherals() {
        List<Peripheral> peripherals = peripheralRepository.findAllWithDetails();
        return peripherals.stream()
                .map(peripheralMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PeripheralResponseDTO getPeripheralById(Long id) {
        return peripheralRepository.findById(id)
                .map(peripheralMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Periférico não encontrado com o ID: " + id));
    }

    @Transactional
    public PeripheralResponseDTO updatePeripheral(Long id, PeripheralUpdateDTO dto) {
        Peripheral entity = peripheralRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Periférico não encontrado com o ID: " + id));

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

        peripheralMapper.updateEntityFromDto(dto, entity);
        
        Long newCollaboratorId = dto.getCollaboratorId();
        Long oldCollaboratorId = (oldCollaborator != null) ? oldCollaborator.getId() : null;
        if (!Objects.equals(oldCollaboratorId, newCollaboratorId)) {
            if (newCollaboratorId == null) {
                entity.setCollaborator(null);
                assetHistoryService.registerEvent(entity, HistoryEventType.DEVOLUCAO, "Periférico desvinculado do colaborador via atualização.", oldCollaborator);
            } else {
                Collaborator newCollaborator = resolver.requireCollaborator(newCollaboratorId);
                entity.setCollaborator(newCollaborator);
                assetHistoryService.registerEvent(entity, HistoryEventType.ALOCACAO, "Periférico alocado para o colaborador " + newCollaborator.getName() + " via atualização.", newCollaborator);
            }
        }
        
        Long newLocationId = dto.getLocationId();
        Long oldLocationId = (oldLocation != null) ? oldLocation.getId() : null;
        if (!Objects.equals(oldLocationId, newLocationId)) {
             if (newLocationId == null) {
                entity.setLocation(null);
                String details = "Periférico desvinculado de sua localização anterior via atualização.";
                if (oldLocation != null) {
                    details = "Periférico removido da localização PA " + oldLocation.getPaNumber() + " via atualização.";
                }
                assetHistoryService.registerEvent(entity, HistoryEventType.DEVOLUCAO, details, null);
            } else {
                Location newLocation = resolver.requireLocation(newLocationId);
                entity.setLocation(newLocation);
                assetHistoryService.registerEvent(entity, HistoryEventType.ALOCACAO, "Periférico alocado para a localização PA " + newLocation.getPaNumber() + " via atualização.", null);
            }
        }

        if (dto.getStatus() != null && !Objects.equals(oldStatus, dto.getStatus())) {
            assetHistoryService.registerEvent(entity, HistoryEventType.ATUALIZACAO, "Status do periférico alterado de '" + oldStatus.name() + "' para '" + dto.getStatus().name() + "'.", null);
        }

        if (dto.getComputerId() != null) {
            entity.setComputer(resolver.optionalComputer(dto.getComputerId()));
        }
        
        Peripheral updated = peripheralRepository.save(entity);
        return peripheralMapper.toResponseDTO(updated);
    }
}
