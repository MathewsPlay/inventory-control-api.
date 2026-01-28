package com.matheuss.controle_estoque_api.service;

import com.matheuss.controle_estoque_api.domain.Collaborator;
import com.matheuss.controle_estoque_api.domain.Computer;
import com.matheuss.controle_estoque_api.domain.Location;
import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import com.matheuss.controle_estoque_api.domain.history.HistoryEventType;
import com.matheuss.controle_estoque_api.dto.ComputerCreateDTO;
import com.matheuss.controle_estoque_api.dto.ComputerResponseDTO;
import com.matheuss.controle_estoque_api.dto.ComputerUpdateDTO;
import com.matheuss.controle_estoque_api.mapper.ComputerMapper;
import com.matheuss.controle_estoque_api.repository.ComputerRepository;
import com.matheuss.controle_estoque_api.service.support.EntityResolver;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ComputerService {

    private final ComputerRepository computerRepository;
    private final ComputerMapper computerMapper;
    private final AssetHistoryService assetHistoryService;
    private final EntityResolver resolver;

    @Transactional
    public ComputerResponseDTO createComputer(ComputerCreateDTO dto) {
        Computer entity = computerMapper.toEntity(dto);

        entity.setCategory(resolver.requireCategory(dto.getCategoryId()));
        entity.setLocation(resolver.optionalLocation(dto.getLocationId()));

        entity.setStatus(AssetStatus.EM_ESTOQUE);
        // CORREÇÃO APLICADA: Garante que o colaborador é nulo na criação.
        entity.setCollaborator(null);

        Computer saved = computerRepository.save(entity);

        assetHistoryService.registerEvent(saved, HistoryEventType.CRIACAO, "Ativo cadastrado no sistema.", null);
        return computerMapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ComputerResponseDTO> getAllComputers() {
        return computerMapper.toResponseDTOList(computerRepository.findAllWithDetails());
    }

    @Transactional(readOnly = true)
    public ComputerResponseDTO getComputerById(Long id) {
        Computer entity = computerRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Computador não encontrado com o ID: " + id));
        return computerMapper.toResponseDTO(entity);
    }

    @Transactional
    public ComputerResponseDTO updateComputer(Long id, ComputerUpdateDTO dto) {
        Computer entity = computerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Computador não encontrado com o ID: " + id));

        // Guarda os valores antigos para comparação
        // CORREÇÃO APLICADA: Usa getCollaborator()
        Collaborator oldCollaborator = entity.getCollaborator();
        Location oldLocation = entity.getLocation();
        AssetStatus oldStatus = entity.getStatus();
        
        // Aplica as atualizações de campos simples do DTO na entidade
        computerMapper.updateEntityFromDto(dto, entity);

        // Lógica de auditoria para mudanças de relacionamento e status
        
        // Verifica mudança de Colaborador
        // CORREÇÃO APLICADA: Usa getCollaboratorId() do DTO
        Long newCollaboratorId = dto.getCollaboratorId();
        Long oldCollaboratorId = (oldCollaborator != null) ? oldCollaborator.getId() : null;
        if (!Objects.equals(oldCollaboratorId, newCollaboratorId)) {
            if (newCollaboratorId == null) {
                // CORREÇÃO APLICADA: Usa setCollaborator(null)
                entity.setCollaborator(null);
                assetHistoryService.registerEvent(entity, HistoryEventType.DEVOLUCAO, "Ativo desvinculado do colaborador via atualização.", oldCollaborator);
            } else {
                // CORREÇÃO APLICADA: Usa requireCollaborator e setCollaborator
                Collaborator newCollaborator = resolver.requireCollaborator(newCollaboratorId);
                entity.setCollaborator(newCollaborator);
                assetHistoryService.registerEvent(entity, HistoryEventType.ALOCACAO, "Ativo alocado para o colaborador " + newCollaborator.getName() + " via atualização.", newCollaborator);
            }
        }

        // Verifica mudança de Localização
        Long newLocationId = dto.getLocationId();
        Long oldLocationId = (oldLocation != null) ? oldLocation.getId() : null;
        if (!Objects.equals(oldLocationId, newLocationId)) {
            if (newLocationId == null) {
                entity.setLocation(null);
                assetHistoryService.registerEvent(entity, HistoryEventType.DEVOLUCAO, "Ativo removido da localização PA " + oldLocation.getPaNumber() + " via atualização.", null);
            } else {
                Location newLocation = resolver.requireLocation(newLocationId);
                entity.setLocation(newLocation);
                assetHistoryService.registerEvent(entity, HistoryEventType.ALOCACAO, "Ativo alocado para a localização PA " + newLocation.getPaNumber() + " via atualização.", null);
            }
        }

        // Verifica mudança de Status
        if (dto.getStatus() != null && !Objects.equals(oldStatus, dto.getStatus())) {
            assetHistoryService.registerEvent(entity, HistoryEventType.ATUALIZACAO, "Status alterado de '" + oldStatus.name() + "' para '" + dto.getStatus().name() + "' via atualização.", null);
        }
        
        // Resolve outras relações (ex: Categoria)
        if (dto.getCategoryId() != null) {
            entity.setCategory(resolver.requireCategory(dto.getCategoryId()));
        }

        // Salva a entidade com todas as alterações aplicadas
        Computer updated = computerRepository.save(entity);
        return computerMapper.toResponseDTO(updated);
    }

}
