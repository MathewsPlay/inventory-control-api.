package com.matheuss.controle_estoque_api.controller;

import com.matheuss.controle_estoque_api.dto.LocationCreateDTO;
import com.matheuss.controle_estoque_api.dto.LocationResponseDTO;
import com.matheuss.controle_estoque_api.dto.LocationUpdateDTO;
import com.matheuss.controle_estoque_api.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor; // Adicionar esta importação
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/locations" )
@RequiredArgsConstructor // Usar injeção de dependência via construtor (melhor prática)
public class LocationController {

    private final LocationService locationService;

    // CREATE
    @PostMapping
    public ResponseEntity<LocationResponseDTO> createLocation(@Valid @RequestBody LocationCreateDTO dto) {
        LocationResponseDTO createdLocation = locationService.createLocation(dto);
        URI locationUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdLocation.getId()).toUri();
        return ResponseEntity.created(locationUri).body(createdLocation);
    }

    // READ (ALL)
    @GetMapping
    public ResponseEntity<List<LocationResponseDTO>> getAllLocations() {
        return ResponseEntity.ok(locationService.findAllLocations());
    }

    // ====================================================================
    // == ENDPOINTS CORRIGIDOS E SIMPLIFICADOS ==
    // ====================================================================

    // READ (BY ID)
    @GetMapping("/{id}")
    public ResponseEntity<LocationResponseDTO> getLocationById(@PathVariable("id") Long id) {
        // Agora o serviço lança a exceção se não encontrar.
        // Se encontrar, o código continua e retorna 200 OK.
        LocationResponseDTO location = locationService.findLocationById(id);
        return ResponseEntity.ok(location);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<LocationResponseDTO> updateLocation(@PathVariable("id") Long id, @Valid @RequestBody LocationUpdateDTO dto) {
        // Lógica simplificada. O serviço cuida do erro "não encontrado".
        LocationResponseDTO updatedLocation = locationService.updateLocation(id, dto);
        return ResponseEntity.ok(updatedLocation);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable("id") Long id) {
        // Removemos o 'if'. O serviço agora lança exceção se não encontrar
        // ou se a localização tiver ativos.
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content em caso de sucesso.
    }
}
