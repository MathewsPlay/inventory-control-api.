package com.matheuss.controle_estoque_api.controller;

import com.matheuss.controle_estoque_api.dto.PeripheralCreateDTO;
import com.matheuss.controle_estoque_api.dto.PeripheralResponseDTO;
import com.matheuss.controle_estoque_api.dto.PeripheralUpdateDTO;
import com.matheuss.controle_estoque_api.service.PeripheralService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/peripherals" )
@RequiredArgsConstructor
public class PeripheralController {

    private final PeripheralService peripheralService;

    @PostMapping
    public ResponseEntity<PeripheralResponseDTO> createPeripheral(@RequestBody @Valid PeripheralCreateDTO dto) {
        PeripheralResponseDTO createdPeripheral = peripheralService.createPeripheral(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdPeripheral.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdPeripheral);
    }

    @GetMapping
    public ResponseEntity<List<PeripheralResponseDTO>> getAllPeripherals() {
        return ResponseEntity.ok(peripheralService.getAllPeripherals());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeripheralResponseDTO> getPeripheralById(@PathVariable Long id) {
        // Simplificado
        return ResponseEntity.ok(peripheralService.getPeripheralById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeripheralResponseDTO> updatePeripheral(@PathVariable Long id, @RequestBody @Valid PeripheralUpdateDTO dto) {
        // Simplificado
        return ResponseEntity.ok(peripheralService.updatePeripheral(id, dto));
    }
}
