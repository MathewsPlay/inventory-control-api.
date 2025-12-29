package com.matheuss.controle_estoque_api.service;

import com.matheuss.controle_estoque_api.domain.Supplier;
import com.matheuss.controle_estoque_api.dto.SupplierCreateDTO;
import com.matheuss.controle_estoque_api.dto.SupplierResponseDTO;
import com.matheuss.controle_estoque_api.dto.SupplierUpdateDTO;
import com.matheuss.controle_estoque_api.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    // CREATE
    @Transactional
    public SupplierResponseDTO createSupplier(SupplierCreateDTO dto) {
        Supplier newSupplier = new Supplier();
        newSupplier.setName(dto.getName());
        newSupplier.setCnpj(dto.getCnpj());
        newSupplier.setEmail(dto.getEmail());
        newSupplier.setPhone(dto.getPhone());
        
        Supplier savedSupplier = supplierRepository.save(newSupplier);
        return toResponseDTO(savedSupplier);
    }

    // READ (ALL)
    @Transactional(readOnly = true)
    public List<SupplierResponseDTO> findAll() {
        return supplierRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // READ (BY ID)
    @Transactional(readOnly = true)
    public Optional<SupplierResponseDTO> findById(Long id) {
        return supplierRepository.findById(id)
                .map(this::toResponseDTO);
    }

    // UPDATE
    @Transactional
    public Optional<SupplierResponseDTO> updateSupplier(Long id, SupplierUpdateDTO dto) {
        return supplierRepository.findById(id).map(existingSupplier -> {
            existingSupplier.setName(dto.getName());
            existingSupplier.setCnpj(dto.getCnpj());
            existingSupplier.setEmail(dto.getEmail());
            existingSupplier.setPhone(dto.getPhone());
            
            Supplier updatedSupplier = supplierRepository.save(existingSupplier);
            return toResponseDTO(updatedSupplier);
        });
    }

    // DELETE
    @Transactional
    public boolean delete(Long id) {
        if (supplierRepository.existsById(id)) {
            supplierRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Método auxiliar para converter Entidade em DTO
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
