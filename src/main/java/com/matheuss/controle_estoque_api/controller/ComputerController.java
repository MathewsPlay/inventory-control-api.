package com.matheuss.controle_estoque_api.controller;

import com.matheuss.controle_estoque_api.domain.enums.AssetStatus; 
import com.matheuss.controle_estoque_api.dto.ComputerCreateDTO;
import com.matheuss.controle_estoque_api.dto.ComputerResponseDTO;
import com.matheuss.controle_estoque_api.dto.ComputerUpdateDTO;
import com.matheuss.controle_estoque_api.dto.SwapComponentRequestDTO;
import com.matheuss.controle_estoque_api.service.ComputerService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/computers" )
public class ComputerController {

    @Autowired
    private ComputerService computerService;

    @PostMapping
    public ResponseEntity<ComputerResponseDTO> createComputer(@RequestBody @Valid ComputerCreateDTO computerDTO) {
        ComputerResponseDTO createdComputer = computerService.createComputer(computerDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdComputer.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdComputer);
    }

    // Método atualizado para receber parâmetros de filtro.
    @GetMapping
@Operation(summary = "Lista computadores com paginação, ordenação e filtros")
public ResponseEntity<Page<ComputerResponseDTO>> getAllComputers(
        // Parâmetros de filtro
        @RequestParam(required = false) AssetStatus status,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String patrimonio,
        @RequestParam(required = false) String serialNumber,
        
        // Paginação
        Pageable pageable) {
    
    Page<ComputerResponseDTO> computersPage = computerService.getAllComputers(status, name, patrimonio, serialNumber, pageable);
    return ResponseEntity.ok(computersPage);
}

    @GetMapping("/{id}")
    public ResponseEntity<ComputerResponseDTO> getComputerById(@PathVariable Long id) {
        ComputerResponseDTO computer = computerService.getComputerById(id);
        return ResponseEntity.ok(computer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComputerResponseDTO> updateComputer(@PathVariable Long id, @RequestBody @Valid ComputerUpdateDTO computerDTO) {
        ComputerResponseDTO updatedComputer = computerService.updateComputer(id, computerDTO);
        return ResponseEntity.ok(updatedComputer);
    }

    @Operation(summary = "Troca um componente instalado em um computador por outro que está em estoque.")
    @PatchMapping("/{computerId}/swap-component")
    public ResponseEntity<ComputerResponseDTO> swapComponent(
            @PathVariable Long computerId,
            @Valid @RequestBody SwapComponentRequestDTO dto) {
        
        ComputerResponseDTO updatedComputer = computerService.swapComponent(
                computerId,
                dto.getComponentToUninstallId(),
                dto.getComponentToInstallId()
        );
        return ResponseEntity.ok(updatedComputer);
    }
}
