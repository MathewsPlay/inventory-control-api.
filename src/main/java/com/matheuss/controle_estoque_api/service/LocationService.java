package com.matheuss.controle_estoque_api.service;

import com.matheuss.controle_estoque_api.domain.Location;
import com.matheuss.controle_estoque_api.dto.LocationCreateDTO;
import com.matheuss.controle_estoque_api.dto.LocationResponseDTO;
import com.matheuss.controle_estoque_api.dto.LocationUpdateDTO;
import com.matheuss.controle_estoque_api.mapper.LocationMapper;
import com.matheuss.controle_estoque_api.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
    public Optional<LocationResponseDTO> findLocationById(Long id) {
        return locationRepository.findById(id).map(locationMapper::toResponseDTO);
    }

    @Transactional
    public Optional<LocationResponseDTO> updateLocation(Long id, LocationUpdateDTO dto) {
        return locationRepository.findById(id).map(existing -> {
            locationMapper.updateEntityFromDto(dto, existing);
            return locationMapper.toResponseDTO(locationRepository.save(existing));
        });
    }

    @Transactional
    public boolean deleteLocation(Long id) {
        if (!locationRepository.existsById(id)) return false;
        locationRepository.deleteById(id);
        return true;
    }
}
