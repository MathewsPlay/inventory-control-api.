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
import com.matheuss.controle_estoque_api.repository.AssetRepository;
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
    private final AssetRepository assetRepository;
    private final PeripheralMapper peripheralMapper;
    private final AssetHistoryService assetHistoryService;
    private final EntityResolver resolver;

    @Transactional
    public PeripheralResponseDTO createPeripheral(PeripheralCreateDTO dto) {
        // LÓGICA EXISTENTE PRESERVADA
        if (dto.getPatrimonio() != null && !dto.getPatrimonio().isBlank() && assetRepository.existsByPatrimonio(dto.getPatrimonio())) {
            throw new IllegalStateException("Já existe um ativo com o número de patrimônio: " + dto.getPatrimonio());
        }
        if (dto.getAssetTag() != null && !dto.getAssetTag().isBlank() && assetRepository.existsByAssetTag(dto.getAssetTag())) {
            throw new IllegalStateException("Já existe um ativo com o Asset Tag: " + dto.getAssetTag());
        }
        
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
        // LÓGICA EXISTENTE PRESERVADA
        List<Peripheral> peripherals = peripheralRepository.findAllWithDetails();
        return peripherals.stream()
                .map(peripheralMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PeripheralResponseDTO getPeripheralById(Long id) {
        // LÓGICA EXISTENTE PRESERVADA
        return peripheralRepository.findById(id)
                .map(peripheralMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Periférico não encontrado com o ID: " + id));
    }

    @Transactional
    public PeripheralResponseDTO updatePeripheral(Long id, PeripheralUpdateDTO dto) {
        // 1. BUSCA DE ENTIDADES
        Peripheral peripheral = resolver.requirePeripheral(id); // Usando o resolver para padronizar
        Location newLocation = resolver.optionalLocation(dto.getLocationId());
        Collaborator newCollaborator = resolver.optionalCollaborator(dto.getCollaboratorId());

        // 2. VALIDAÇÃO DE UNICIDADE (Sua lógica, preservada)
        if (dto.getPatrimonio() != null && !dto.getPatrimonio().isBlank() && !Objects.equals(peripheral.getPatrimonio(), dto.getPatrimonio())) {
            if (assetRepository.existsByPatrimonio(dto.getPatrimonio())) {
                throw new IllegalStateException("Operação não permitida: Já existe outro ativo com o patrimônio: " + dto.getPatrimonio());
            }
        }
        if (dto.getAssetTag() != null && !dto.getAssetTag().isBlank() && !Objects.equals(peripheral.getAssetTag(), dto.getAssetTag())) {
            if (assetRepository.existsByAssetTag(dto.getAssetTag())) {
                throw new IllegalStateException("Operação não permitida: Já existe outro ativo com o Asset Tag: " + dto.getAssetTag());
            }
        }

        // 3. ATUALIZAÇÃO DOS CAMPOS SIMPLES
        // O Mapper NÃO deve mais atualizar location, collaborator e status.
        peripheralMapper.updateEntityFromDto(dto, peripheral);
        if (dto.getCategoryId() != null) {
            peripheral.setCategory(resolver.requireCategory(dto.getCategoryId()));
        }
        // A associação com o computador é tratada como um campo simples aqui
        if (dto.getComputerId() != null) {
            peripheral.setComputer(resolver.optionalComputer(dto.getComputerId()));
        }

        // 4. LÓGICA DE ESTADO E ALOCAÇÃO
        Location oldLocation = peripheral.getLocation();
        Collaborator oldCollaborator = peripheral.getCollaborator();

        if (newLocation != null && newCollaborator != null) {
            throw new IllegalStateException("Operação não permitida: Um ativo não pode ser alocado para um colaborador e uma localização ao mesmo tempo.");
        }

        peripheral.setLocation(newLocation);
        peripheral.setCollaborator(newCollaborator);

        if (newLocation != null || newCollaborator != null) {
            peripheral.setStatus(AssetStatus.EM_USO);
        } else {
            peripheral.setStatus(AssetStatus.EM_ESTOQUE);
        }

        // 5. REGISTRO DE HISTÓRICO INTELIGENTE
        assetHistoryService.registerEvent(peripheral, HistoryEventType.ATUALIZACAO, "Dados do ativo foram atualizados.", null);

        if (!Objects.equals(oldLocation, newLocation)) {
            if (newLocation != null) {
               assetHistoryService.registerEvent(peripheral, HistoryEventType.ALOCACAO, "Ativo alocado para a localização PA: " + newLocation.getPaNumber(), null);
            }
            if (oldLocation != null) {
                assetHistoryService.registerEvent(peripheral, HistoryEventType.DEVOLUCAO, "Ativo devolvido da localização PA: " + oldLocation.getPaNumber(), null);
            }
        }
        if (!Objects.equals(oldCollaborator, newCollaborator)) {
            if (newCollaborator != null) {
                assetHistoryService.registerEvent(peripheral, HistoryEventType.ALOCACAO, "Ativo alocado para o colaborador: " + newCollaborator.getName(), null);
            }
            if (oldCollaborator != null) {
                assetHistoryService.registerEvent(peripheral, HistoryEventType.DEVOLUCAO, "Ativo devolvido pelo colaborador: " + oldCollaborator.getName(), null);
            }
        }

        // 6. SALVAR E RETORNAR
        Peripheral updatedPeripheral = peripheralRepository.save(peripheral);
        return peripheralMapper.toResponseDTO(updatedPeripheral);
    }
}
