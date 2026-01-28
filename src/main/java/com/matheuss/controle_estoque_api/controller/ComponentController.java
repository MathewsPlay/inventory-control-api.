package com.matheuss.controle_estoque_api.controller;

import com.matheuss.controle_estoque_api.dto.ComponentCreateDTO;
import com.matheuss.controle_estoque_api.dto.ComponentResponseDTO;
import com.matheuss.controle_estoque_api.dto.ComponentUpdateDTO;
import com.matheuss.controle_estoque_api.service.ComponentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/components" )
@RequiredArgsConstructor
public class ComponentController {

    private final ComponentService componentService;

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

    @GetMapping
    public ResponseEntity<List<ComponentResponseDTO>> getAllComponents() {
        List<ComponentResponseDTO> components = componentService.getAllComponents();
        return ResponseEntity.ok(components);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponentResponseDTO> getComponentById(@PathVariable Long id) {
        // Simplificado: o serviço agora lança exceção se não encontrar
        return ResponseEntity.ok(componentService.getComponentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComponentResponseDTO> updateComponent(@PathVariable Long id, @RequestBody @Valid ComponentUpdateDTO dto) {
        // Simplificado: o serviço agora lança exceção se não encontrar
        return ResponseEntity.ok(componentService.updateComponent(id, dto));
    }
}
