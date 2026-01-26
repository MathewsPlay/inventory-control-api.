package com.matheuss.controle_estoque_api.service;

import com.matheuss.controle_estoque_api.domain.Collaborator;
import com.matheuss.controle_estoque_api.dto.CollaboratorCreateDTO;
import com.matheuss.controle_estoque_api.dto.CollaboratorResponseDTO;
import com.matheuss.controle_estoque_api.dto.CollaboratorUpdateDTO;
import com.matheuss.controle_estoque_api.mapper.CollaboratorMapper;
import com.matheuss.controle_estoque_api.repository.CollaboratorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;
    private final CollaboratorMapper collaboratorMapper;

    // COMMANDS

    @Transactional
    public CollaboratorResponseDTO create(CollaboratorCreateDTO dto) {
        Collaborator entity = collaboratorMapper.toEntity(dto);
        Collaborator saved = collaboratorRepository.save(entity);
        return collaboratorMapper.toResponseDTO(saved);
    }

    // QUERIES

    @Transactional(readOnly = true)
    public List<CollaboratorResponseDTO> getAll() {
        return collaboratorRepository.findAll().stream()
                .map(collaboratorMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CollaboratorResponseDTO getById(Long id) {
        return collaboratorRepository.findById(id)
                .map(collaboratorMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Colaborador não encontrado com o ID: " + id));
    }

    // COMMANDS

    @Transactional
    public CollaboratorResponseDTO update(Long id, CollaboratorUpdateDTO dto) {
        Collaborator entity = collaboratorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Colaborador não encontrado com o ID: " + id));

        collaboratorMapper.updateEntityFromDto(dto, entity);

        Collaborator updated = collaboratorRepository.save(entity);
        return collaboratorMapper.toResponseDTO(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!collaboratorRepository.existsById(id)) {
            throw new EntityNotFoundException("Colaborador não encontrado com o ID: " + id);
        }
        collaboratorRepository.deleteById(id);
    }
}
