package com.matheuss.controle_estoque_api.service;

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
        entity.setUser(null);

        Peripheral saved = peripheralRepository.save(entity);
        assetHistoryService.registerEvent(saved, HistoryEventType.CRIACAO, "Ativo cadastrado no sistema.", null);

        return peripheralMapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<PeripheralResponseDTO> getAllPeripherals() {
        return peripheralRepository.findAll().stream()
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

        peripheralMapper.updateEntityFromDto(dto, entity);

        if (dto.getLocationId() != null) entity.setLocation(resolver.optionalLocation(dto.getLocationId()));
        if (dto.getComputerId() != null) entity.setComputer(resolver.optionalComputer(dto.getComputerId()));

        Peripheral updated = peripheralRepository.save(entity);
        return peripheralMapper.toResponseDTO(updated);
    }

    @Transactional
    public void deletePeripheral(Long id) {
        if (!peripheralRepository.existsById(id)) {
            throw new EntityNotFoundException("Periférico não encontrado com o ID: " + id);
        }
        peripheralRepository.deleteById(id);
    }
}
