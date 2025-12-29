package com.matheuss.controle_estoque_api.service;

import com.matheuss.controle_estoque_api.domain.Category;
import com.matheuss.controle_estoque_api.domain.Computer;
import com.matheuss.controle_estoque_api.domain.Location;
import com.matheuss.controle_estoque_api.domain.Supplier;
import com.matheuss.controle_estoque_api.dto.*;
import com.matheuss.controle_estoque_api.repository.CategoryRepository;
import com.matheuss.controle_estoque_api.repository.ComputerRepository;
import com.matheuss.controle_estoque_api.repository.LocationRepository;
import com.matheuss.controle_estoque_api.repository.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComputerService {

    @Autowired
    private ComputerRepository computerRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private LocationRepository locationRepository; // <-- NOVA DEPENDÊNCIA

    // CREATE
    @Transactional
    public ComputerResponseDTO createComputer(ComputerCreateDTO dto) {
        Computer newComputer = new Computer();
        
        // Mapeamento dos relacionamentos
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com o ID: " + dto.getCategoryId()));
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado com o ID: " + dto.getSupplierId()));
        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new EntityNotFoundException("Localização não encontrada com o ID: " + dto.getLocationId()));

        newComputer.setCategory(category);
        newComputer.setSupplier(supplier);
        newComputer.setLocation(location); // <-- ASSOCIA A LOCALIZAÇÃO

        // Mapeamento dos outros campos
        newComputer.setAssetTag(dto.getAssetTag());
        newComputer.setStatus(dto.getStatus());
        newComputer.setPurchaseDate(dto.getPurchaseDate());
        newComputer.setName(dto.getName());
        newComputer.setSerialNumber(dto.getSerialNumber());
        newComputer.setCpu(dto.getCpu());
        newComputer.setRamSizeInGB(dto.getRamSizeInGB());
        newComputer.setStorageSizeInGB(dto.getStorageSizeInGB());
        newComputer.setOs(dto.getOs());

        Computer savedComputer = computerRepository.save(newComputer);
        return toResponseDTO(savedComputer);
    }

    // READ (ALL)
    @Transactional(readOnly = true)
    public List<ComputerResponseDTO> findAllComputers() {
        return computerRepository.findAllWithDetails()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // READ (BY ID)
    @Transactional(readOnly = true)
    public Optional<ComputerResponseDTO> findComputerById(Long id) {
        return computerRepository.findByIdWithDetails(id)
                .map(this::toResponseDTO);
    }

    // UPDATE
    @Transactional
    public Optional<ComputerResponseDTO> updateComputer(Long id, ComputerUpdateDTO dto) {
        return computerRepository.findById(id).map(existingComputer -> {
            // Atualiza relacionamentos
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com o ID: " + dto.getCategoryId()));
            Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                    .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado com o ID: " + dto.getSupplierId()));
            Location location = locationRepository.findById(dto.getLocationId())
                    .orElseThrow(() -> new EntityNotFoundException("Localização não encontrada com o ID: " + dto.getLocationId()));

            existingComputer.setCategory(category);
            existingComputer.setSupplier(supplier);
            existingComputer.setLocation(location); // <-- ATUALIZA A LOCALIZAÇÃO

            // Atualiza outros campos
            existingComputer.setAssetTag(dto.getAssetTag());
            existingComputer.setStatus(dto.getStatus());
            existingComputer.setPurchaseDate(dto.getPurchaseDate());
            existingComputer.setName(dto.getName());
            existingComputer.setSerialNumber(dto.getSerialNumber());
            existingComputer.setCpu(dto.getCpu());
            existingComputer.setRamSizeInGB(dto.getRamSizeInGB());
            existingComputer.setStorageSizeInGB(dto.getStorageSizeInGB());
            existingComputer.setOs(dto.getOs());

            Computer updatedComputer = computerRepository.save(existingComputer);
            return toResponseDTO(updatedComputer);
        });
    }

    // DELETE
    @Transactional
    public boolean deleteComputer(Long id) {
        if (computerRepository.existsById(id)) {
            computerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Método de mapeamento para DTO
    private ComputerResponseDTO toResponseDTO(Computer computer) {
        ComputerResponseDTO dto = new ComputerResponseDTO();
        dto.setId(computer.getId());
        dto.setAssetTag(computer.getAssetTag());
        dto.setStatus(computer.getStatus());
        dto.setPurchaseDate(computer.getPurchaseDate());
        dto.setName(computer.getName());
        dto.setSerialNumber(computer.getSerialNumber());
        dto.setCpu(computer.getCpu());
        dto.setRamSizeInGB(computer.getRamSizeInGB());
        dto.setStorageSizeInGB(computer.getStorageSizeInGB());
        dto.setOs(computer.getOs());

        // Mapeamento dos DTOs aninhados
        if (computer.getCategory() != null) {
            CategoryResponseDTO categoryDto = new CategoryResponseDTO();
            categoryDto.setId(computer.getCategory().getId());
            categoryDto.setName(computer.getCategory().getName());
            dto.setCategory(categoryDto);
        }

        if (computer.getSupplier() != null) {
            SupplierResponseDTO supplierDto = new SupplierResponseDTO();
            supplierDto.setId(computer.getSupplier().getId());
            supplierDto.setName(computer.getSupplier().getName());
            dto.setSupplier(supplierDto);
        }

        if (computer.getLocation() != null) { // <-- LÓGICA NOVA
            LocationResponseDTO locationDto = new LocationResponseDTO();
            locationDto.setId(computer.getLocation().getId());
            locationDto.setPaNumber(computer.getLocation().getPaNumber());
            locationDto.setFloor(computer.getLocation().getFloor());
            locationDto.setSector(computer.getLocation().getSector());
            dto.setLocation(locationDto);
        }

        return dto;
    }
}
