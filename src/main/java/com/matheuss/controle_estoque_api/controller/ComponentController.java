package com.matheuss.controle_estoque_api.controller;

import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import com.matheuss.controle_estoque_api.dto.ComponentCreateDTO;
import com.matheuss.controle_estoque_api.dto.ComponentResponseDTO;
import com.matheuss.controle_estoque_api.dto.ComponentUpdateDTO;
import com.matheuss.controle_estoque_api.service.ComponentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/components" )
@Tag(name = "Components")
@RequiredArgsConstructor
public class ComponentController {

    private final ComponentService componentService;

    @Operation(summary = "Cria um novo componente")
    @PostMapping
    public ResponseEntity<ComponentResponseDTO> createComponent(@RequestBody @Valid ComponentCreateDTO dto) {
        // LÓGICA EXISTENTE PRESERVADA
        ComponentResponseDTO createdComponent = componentService.createComponent(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdComponent.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdComponent);
    }

    // ====================================================================
    // == MÉTODO GETALL ATUALIZADO PARA PAGINAÇÃO ==
    // ====================================================================
   @GetMapping
@Operation(summary = "Lista componentes com paginação, ordenação e filtros")
public ResponseEntity<Page<ComponentResponseDTO>> getAllComponents(
        // Parâmetros de filtro
        @RequestParam(required = false) AssetStatus status,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String name,
        
        // Paginação
        Pageable pageable) {
    
    Page<ComponentResponseDTO> componentsPage = componentService.getAllComponents(status, type, name, pageable);
    return ResponseEntity.ok(componentsPage);
}

    @Operation(summary = "Busca um componente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ComponentResponseDTO> getComponentById(@PathVariable Long id) {
        // LÓGICA EXISTENTE PRESERVADA
        return ResponseEntity.ok(componentService.getComponentById(id));
    }

    @Operation(summary = "Atualiza um componente por ID")
    @PutMapping("/{id}")
    public ResponseEntity<ComponentResponseDTO> updateComponent(@PathVariable Long id, @RequestBody @Valid ComponentUpdateDTO dto) {
        // LÓGICA EXISTENTE PRESERVADA
        return ResponseEntity.ok(componentService.updateComponent(id, dto));
    }

    @Operation(summary = "Instala um componente em um computador")
    @PatchMapping("/{componentId}/install/{computerId}")
    public ResponseEntity<ComponentResponseDTO> installComponent(
            @PathVariable Long componentId,
            @PathVariable Long computerId) {
        // LÓGICA EXISTENTE PRESERVADA
        ComponentResponseDTO updatedComponent = componentService.installComponent(componentId, computerId);
        return ResponseEntity.ok(updatedComponent);
    }
}
