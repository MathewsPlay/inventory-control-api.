package com.matheuss.controle_estoque_api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Adicionar este import

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matheuss.controle_estoque_api.domain.Supplier;
import com.matheuss.controle_estoque_api.dto.SupplierCreateDTO;
import com.matheuss.controle_estoque_api.dto.SupplierResponseDTO; // Adicionar este import
import com.matheuss.controle_estoque_api.repository.SupplierRepository;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    // --- CREATE ---
    @Transactional
    public SupplierResponseDTO createSupplier(SupplierCreateDTO dto) {
        Supplier newSupplier = new Supplier();
        newSupplier.setName(dto.getName());
        newSupplier.setCnpj(dto.getCnpj());
        newSupplier.setEmail(dto.getEmail());
        newSupplier.setPhone(dto.getPhone());
        
        Supplier savedSupplier = supplierRepository.save(newSupplier);
        return toResponseDTO(savedSupplier); // Retorna DTO
    }

    // --- READ ---
    @Transactional(readOnly = true)
    public List<SupplierResponseDTO> findAll() {
        return supplierRepository.findAll()
                .stream()
                .map(this::toResponseDTO) // Converte cada um para DTO
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<SupplierResponseDTO> findById(Long id) {
        return supplierRepository.findById(id)
                .map(this::toResponseDTO); // Converte para DTO se encontrar
    }

    // --- DELETE ---
    @Transactional
    public boolean delete(Long id) {
        if (supplierRepository.existsById(id)) {
            supplierRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // --- MÉTODO DE MAPEAMENTO MANUAL ---
    private SupplierResponseDTO toResponseDTO(Supplier supplier) {
        SupplierResponseDTO dto = new SupplierResponseDTO();
        dto.setId(supplier.getId());
        dto.setName(supplier.getName());
        dto.setCnpj(supplier.getCnpj());
        dto.setEmail(supplier.getEmail());
        dto.setPhone(supplier.getPhone());
        return dto;
    }
}
