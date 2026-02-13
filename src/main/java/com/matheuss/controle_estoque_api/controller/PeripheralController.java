package com.matheuss.controle_estoque_api.controller;

import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import com.matheuss.controle_estoque_api.dto.PeripheralCreateDTO;
import com.matheuss.controle_estoque_api.dto.PeripheralResponseDTO;
import com.matheuss.controle_estoque_api.dto.PeripheralUpdateDTO;
import com.matheuss.controle_estoque_api.service.PeripheralService;
import io.swagger.v3.oas.annotations.Operation; // Import adicionado
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page; // Import adicionado
import org.springframework.data.domain.Pageable; // Import adicionado
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
// import java.util.List; // Este import não é mais necessário para o getAll

@RestController
@RequestMapping("/api/peripherals" )
@RequiredArgsConstructor
public class PeripheralController {

    private final PeripheralService peripheralService;

    @PostMapping
    public ResponseEntity<PeripheralResponseDTO> createPeripheral(@RequestBody @Valid PeripheralCreateDTO dto) {
        // LÓGICA EXISTENTE PRESERVADA
        PeripheralResponseDTO createdPeripheral = peripheralService.createPeripheral(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdPeripheral.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdPeripheral);
    }

    // ====================================================================
    // == MÉTODO GETALL ATUALIZADO PARA PAGINAÇÃO ==
    // ====================================================================
    @GetMapping
@Operation(summary = "Lista periféricos com paginação, ordenação e filtros")
public ResponseEntity<Page<PeripheralResponseDTO>> getAllPeripherals(
        // Parâmetros de filtro
        @RequestParam(required = false) AssetStatus status,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String name,
        
        // Paginação
        Pageable pageable) {
    
    Page<PeripheralResponseDTO> peripheralsPage = peripheralService.getAllPeripherals(status, type, name, pageable);
    return ResponseEntity.ok(peripheralsPage);
}

    @GetMapping("/{id}")
    public ResponseEntity<PeripheralResponseDTO> getPeripheralById(@PathVariable Long id) {
        // LÓGICA EXISTENTE PRESERVADA
        return ResponseEntity.ok(peripheralService.getPeripheralById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeripheralResponseDTO> updatePeripheral(@PathVariable Long id, @RequestBody @Valid PeripheralUpdateDTO dto) {
        // LÓGICA EXISTENTE PRESERVADA
        return ResponseEntity.ok(peripheralService.updatePeripheral(id, dto));
    }
}
