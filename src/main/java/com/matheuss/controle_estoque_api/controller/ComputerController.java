package com.matheuss.controle_estoque_api.controller;

import com.matheuss.controle_estoque_api.dto.ComputerCreateDTO;
import com.matheuss.controle_estoque_api.dto.ComputerResponseDTO;
import com.matheuss.controle_estoque_api.dto.ComputerUpdateDTO;
import com.matheuss.controle_estoque_api.service.ComputerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<ComputerResponseDTO>> getAllComputers() {
        List<ComputerResponseDTO> computers = computerService.getAllComputers();
        return ResponseEntity.ok(computers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComputerResponseDTO> getComputerById(@PathVariable Long id) {
        ComputerResponseDTO computer = computerService.getComputerById(id);
        return ResponseEntity.ok(computer);
    }

    // ====================================================================
    // == ENDPOINT DE ATUALIZAÇÃO REFINADO ==
    // ====================================================================
    @PutMapping("/{id}")
    public ResponseEntity<ComputerResponseDTO> updateComputer(@PathVariable Long id, @RequestBody @Valid ComputerUpdateDTO computerDTO) {
        ComputerResponseDTO updatedComputer = computerService.updateComputer(id, computerDTO);
        return ResponseEntity.ok(updatedComputer);
    }
}
