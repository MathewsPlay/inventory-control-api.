package com.matheuss.controle_estoque_api.service;

import com.matheuss.controle_estoque_api.domain.Computer;
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

        // Resolver relacionamentos aqui (sem mapper buscar no banco)
        entity.setCategory(resolver.requireCategory(dto.getCategoryId()));
        entity.setLocation(resolver.optionalLocation(dto.getLocationId()));

        // Cadastro entra como EM_ESTOQUE (movimentação é outro caso de uso)
        entity.setStatus(AssetStatus.EM_ESTOQUE);
        entity.setUser(null);

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

        // Atualiza campos simples
        computerMapper.updateEntityFromDto(dto, entity);

        // Resolver relações somente se vierem IDs
        if (dto.getCategoryId() != null) entity.setCategory(resolver.requireCategory(dto.getCategoryId()));
        if (dto.getLocationId() != null) entity.setLocation(resolver.optionalLocation(dto.getLocationId()));

        // Evite permitir status via update livre (ideal: endpoints de movimentação)
        // Aqui você pode manter, mas eu recomendo remover de ComputerUpdateDTO no futuro.
        // if (dto.getStatus() != null) entity.setStatus(dto.getStatus());

        Computer updated = computerRepository.save(entity);
        return computerMapper.toResponseDTO(updated);
    }

    @Transactional
    public boolean deleteComputer(Long id) {
        if (!computerRepository.existsById(id)) return false;
        computerRepository.deleteById(id);
        return true;
    }
}
