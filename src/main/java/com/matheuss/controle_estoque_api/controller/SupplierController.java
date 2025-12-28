package com.matheuss.controle_estoque_api.controller;

import com.matheuss.controle_estoque_api.dto.SupplierCreateDTO;
import com.matheuss.controle_estoque_api.dto.SupplierResponseDTO; // 1. Importa o DTO de resposta
import com.matheuss.controle_estoque_api.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // 2. Importa o HttpStatus
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/suppliers" ) // 3. Padroniza a URL base com "/api"
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    // --- CREATE ---
    @PostMapping
    public ResponseEntity<SupplierResponseDTO> createSupplier(@RequestBody SupplierCreateDTO supplierDTO) {
        // O serviço agora retorna o DTO de resposta diretamente
        SupplierResponseDTO createdSupplier = supplierService.createSupplier(supplierDTO);

        // Cria a URI para o novo recurso criado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdSupplier.getId()) // Usa o ID do DTO retornado
                .toUri();
        
        // Retorna 201 Created, a localização e o objeto DTO no corpo da resposta
        return ResponseEntity.created(location).body(createdSupplier);
    }

    // --- READ (ALL) ---
    @GetMapping
    public ResponseEntity<List<SupplierResponseDTO>> getAllSuppliers() {
        // O serviço agora retorna uma lista de DTOs
        List<SupplierResponseDTO> suppliers = supplierService.findAll();
        return ResponseEntity.ok(suppliers);
    }

    // --- READ (BY ID) ---
@GetMapping("/{id}")
public ResponseEntity<SupplierResponseDTO> getSupplierById(@PathVariable("id") Long id) { // <-- CORREÇÃO APLICADA
    // O serviço agora retorna um Optional de DTO
    return supplierService.findById(id)
            .map(ResponseEntity::ok) // Se encontrar, retorna 200 OK com o DTO
            .orElse(ResponseEntity.notFound().build()); // Se não, retorna 404 Not Found
}
    // --- DELETE ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        boolean wasDeleted = supplierService.delete(id);
        if (wasDeleted) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
    
    // --- UPDATE ---
    // O método de UPDATE será nosso próximo passo. Por enquanto, vamos garantir que o CRUD básico com DTOs funciona.
}
