package com.matheuss.controle_estoque_api.controller;

import com.matheuss.controle_estoque_api.dto.ComputerCreateDTO;
import com.matheuss.controle_estoque_api.dto.ComputerResponseDTO;
import com.matheuss.controle_estoque_api.dto.ComputerUpdateDTO;
import com.matheuss.controle_estoque_api.service.ComputerService;
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

    // --- CREATE ---
    @PostMapping
    public ResponseEntity<ComputerResponseDTO> createComputer(@RequestBody ComputerCreateDTO computerDTO) {
        ComputerResponseDTO createdComputer = computerService.createComputer(computerDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdComputer.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdComputer);
    }

    // --- READ (ALL) ---
    @GetMapping
    public ResponseEntity<List<ComputerResponseDTO>> getAllComputers() {
        List<ComputerResponseDTO> computers = computerService.findAllComputers();
        return ResponseEntity.ok(computers);
    }

    // --- READ (BY ID) ---
    // ESTE É O MÉTODO QUE PROVAVELMENTE ESTAVA COM O ERRO DE SINTAXE
    @GetMapping("/{id}")
    public ResponseEntity<ComputerResponseDTO> getComputerById(@PathVariable("id") Long id) { // Correção do @PathVariable
        return computerService.findComputerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // Ponto e vírgula garantido aqui
    }

    // --- UPDATE ---
    @PutMapping("/{id}")
    public ResponseEntity<ComputerResponseDTO> updateComputer(@PathVariable("id") Long id, @RequestBody ComputerUpdateDTO computerDTO) {
        return computerService.updateComputer(id, computerDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- DELETE ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComputer(@PathVariable("id") Long id) {
        if (computerService.deleteComputer(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
