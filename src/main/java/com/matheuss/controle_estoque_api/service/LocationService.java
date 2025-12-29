package com.matheuss.controle_estoque_api.service;

import com.matheuss.controle_estoque_api.domain.Location;
import com.matheuss.controle_estoque_api.dto.LocationCreateDTO;
import com.matheuss.controle_estoque_api.dto.LocationResponseDTO;
import com.matheuss.controle_estoque_api.dto.LocationUpdateDTO;
import com.matheuss.controle_estoque_api.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    // CREATE
    @Transactional
    public LocationResponseDTO createLocation(LocationCreateDTO dto) {
        Location newLocation = new Location();
        newLocation.setPaNumber(dto.getPaNumber());
        newLocation.setFloor(dto.getFloor());
        newLocation.setSector(dto.getSector());
        newLocation.setDescription(dto.getDescription());

        Location savedLocation = locationRepository.save(newLocation);
        return toResponseDTO(savedLocation);
    }

    // READ (ALL)
    @Transactional(readOnly = true)
    public List<LocationResponseDTO> findAllLocations() {
        return locationRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // READ (BY ID)
    @Transactional(readOnly = true)
    public Optional<LocationResponseDTO> findLocationById(Long id) {
        return locationRepository.findById(id)
                .map(this::toResponseDTO);
    }

    // UPDATE
    @Transactional
    public Optional<LocationResponseDTO> updateLocation(Long id, LocationUpdateDTO dto) {
        return locationRepository.findById(id).map(existingLocation -> {
            existingLocation.setPaNumber(dto.getPaNumber());
            existingLocation.setFloor(dto.getFloor());
            existingLocation.setSector(dto.getSector());
            existingLocation.setDescription(dto.getDescription());
            
            Location updatedLocation = locationRepository.save(existingLocation);
            return toResponseDTO(updatedLocation);
        });
    }

    // DELETE
    @Transactional
    public boolean deleteLocation(Long id) {
        if (locationRepository.existsById(id)) {
            locationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Método auxiliar para converter Entidade em DTO
    private LocationResponseDTO toResponseDTO(Location location) {
        LocationResponseDTO dto = new LocationResponseDTO();
        dto.setId(location.getId());
        dto.setPaNumber(location.getPaNumber());
        dto.setFloor(location.getFloor());
        dto.setSector(location.getSector());
        dto.setDescription(location.getDescription());
        return dto;
    }
}
