package com.matheuss.controle_estoque_api.service;

import com.matheuss.controle_estoque_api.domain.Location;
import com.matheuss.controle_estoque_api.dto.LocationCreateDTO;
import com.matheuss.controle_estoque_api.dto.LocationResponseDTO;
import com.matheuss.controle_estoque_api.dto.LocationUpdateDTO;
import com.matheuss.controle_estoque_api.mapper.LocationMapper;
import com.matheuss.controle_estoque_api.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException; // Importar
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Transactional
    public LocationResponseDTO createLocation(LocationCreateDTO dto) {
        Location entity = locationMapper.toEntity(dto);
        Location saved = locationRepository.save(entity);
        return locationMapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<LocationResponseDTO> findAllLocations() {
        return locationRepository.findAll().stream()
                .map(locationMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LocationResponseDTO findLocationById(Long id) {
        // PADRONIZAÇÃO: Lançar exceção se não encontrar
        return locationRepository.findById(id)
                .map(locationMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Localização não encontrada com o ID: " + id));
    }

    @Transactional
    public LocationResponseDTO updateLocation(Long id, LocationUpdateDTO dto) {
        // PADRONIZAÇÃO: Lançar exceção se não encontrar
        Location existing = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Localização não encontrada com o ID: " + id));
        
        locationMapper.updateEntityFromDto(dto, existing);
        Location saved = locationRepository.save(existing);
        return locationMapper.toResponseDTO(saved);
    }

    // ====================================================================
    // == MÉTODO DELETE REATORADO (MAIS SEGURO) ==
    // ====================================================================
    @Transactional
    public void deleteLocation(Long id) {
        // 1. Busca a localização incluindo a lista de ativos para verificação
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Localização não encontrada com o ID: " + id));

        // 2. VERIFICAÇÃO DE SEGURANÇA
        // O ideal é fazer uma consulta para verificar se existem ativos.
        // Acessar location.getAssets().isEmpty() pode ser ineficiente se a lista for grande.
        // Uma abordagem melhor seria ter um método no repository.
        // Por simplicidade aqui, vamos usar o getAssets(), mas ciente da performance.
        if (location.getAssets() != null && !location.getAssets().isEmpty()) {
            throw new IllegalStateException(
                "Operação não permitida: A localização PA " + location.getPaNumber() + 
                " não pode ser deletada pois possui " + location.getAssets().size() + " ativo(s) associado(s)."
            );
        }

        // 3. Se a verificação passar, deleta a localização
        locationRepository.delete(location);
    }
}
