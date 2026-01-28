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
    private final PeripheralMapper peripheralMapper;
    private final AssetHistoryService assetHistoryService;
    private final EntityResolver resolver;

    @Transactional
    public PeripheralResponseDTO createPeripheral(PeripheralCreateDTO dto) {
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
        // ATUALIZAÇÃO: Usando o método otimizado para evitar N+1 queries
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

        // Guarda estados antigos para comparação
        Collaborator oldCollaborator = entity.getCollaborator();
        Location oldLocation = entity.getLocation();
        AssetStatus oldStatus = entity.getStatus();

        // Aplica atualizações simples
        peripheralMapper.updateEntityFromDto(dto, entity);

        // Lógica de auditoria para relacionamentos e status
        
        // Verifica mudança de Colaborador
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
        
        // Verifica mudança de Localização
        Long newLocationId = dto.getLocationId();
        Long oldLocationId = (oldLocation != null) ? oldLocation.getId() : null;
        if (!Objects.equals(oldLocationId, newLocationId)) {
             if (newLocationId == null) {
                entity.setLocation(null);
                assetHistoryService.registerEvent(entity, HistoryEventType.DEVOLUCAO, "Periférico removido da localização PA " + oldLocation.getPaNumber() + " via atualização.", null);
            } else {
                Location newLocation = resolver.requireLocation(newLocationId);
                entity.setLocation(newLocation);
                assetHistoryService.registerEvent(entity, HistoryEventType.ALOCACAO, "Periférico alocado para a localização PA " + newLocation.getPaNumber() + " via atualização.", null);
            }
        }

        // Verifica mudança de Status
        if (dto.getStatus() != null && !Objects.equals(oldStatus, dto.getStatus())) {
            assetHistoryService.registerEvent(entity, HistoryEventType.ATUALIZACAO, "Status do periférico alterado de '" + oldStatus.name() + "' para '" + dto.getStatus().name() + "'.", null);
        }

        // Resolve outras relações
        if (dto.getComputerId() != null) {
            entity.setComputer(resolver.optionalComputer(dto.getComputerId()));
        }
        
        Peripheral updated = peripheralRepository.save(entity);
        return peripheralMapper.toResponseDTO(updated);
    }

    // O método deletePeripheral() foi removido em favor da estratégia de "Soft Delete".
}
