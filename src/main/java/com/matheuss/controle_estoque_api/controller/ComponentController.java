package com.matheuss.controle_estoque_api.controller;

import com.matheuss.controle_estoque_api.dto.ComponentCreateDTO;
import com.matheuss.controle_estoque_api.dto.ComponentResponseDTO;
import com.matheuss.controle_estoque_api.dto.ComponentUpdateDTO;
import com.matheuss.controle_estoque_api.service.ComponentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/components" )
@Tag(name = "Components") // Adicionando a Tag do Swagger para organização
@RequiredArgsConstructor
public class ComponentController {

    private final ComponentService componentService;

    @Operation(summary = "Cria um novo componente")
    @PostMapping
    public ResponseEntity<ComponentResponseDTO> createComponent(@RequestBody @Valid ComponentCreateDTO dto) {
        ComponentResponseDTO createdComponent = componentService.createComponent(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdComponent.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdComponent);
    }

    @Operation(summary = "Lista todos os componentes")
    @GetMapping
    public ResponseEntity<List<ComponentResponseDTO>> getAllComponents() {
        List<ComponentResponseDTO> components = componentService.getAllComponents();
        return ResponseEntity.ok(components);
    }

    @Operation(summary = "Busca um componente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ComponentResponseDTO> getComponentById(@PathVariable Long id) {
        return ResponseEntity.ok(componentService.getComponentById(id));
    }

    @Operation(summary = "Atualiza um componente por ID")
    @PutMapping("/{id}")
    public ResponseEntity<ComponentResponseDTO> updateComponent(@PathVariable Long id, @RequestBody @Valid ComponentUpdateDTO dto) {
        return ResponseEntity.ok(componentService.updateComponent(id, dto));
    }

    @Operation(summary = "Instala um componente em um computador")
    @PatchMapping("/{componentId}/install/{computerId}")
    public ResponseEntity<ComponentResponseDTO> installComponent(
            @PathVariable Long componentId,
            @PathVariable Long computerId) {
        // CORREÇÃO: O método de serviço retorna um ComponentResponseDTO, então o declaramos aqui.
        ComponentResponseDTO updatedComponent = componentService.installComponent(componentId, computerId);
        return ResponseEntity.ok(updatedComponent);
    }
}
