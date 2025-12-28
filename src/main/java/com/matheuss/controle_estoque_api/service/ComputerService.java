package com.matheuss.controle_estoque_api.service;

import com.matheuss.controle_estoque_api.domain.Category;
import com.matheuss.controle_estoque_api.domain.Computer;
import com.matheuss.controle_estoque_api.domain.Supplier;
import com.matheuss.controle_estoque_api.dto.CategoryResponseDTO;
import com.matheuss.controle_estoque_api.dto.ComputerCreateDTO;
import com.matheuss.controle_estoque_api.dto.ComputerResponseDTO;
import com.matheuss.controle_estoque_api.dto.ComputerUpdateDTO;
import com.matheuss.controle_estoque_api.dto.SupplierResponseDTO;
import com.matheuss.controle_estoque_api.repository.CategoryRepository;
import com.matheuss.controle_estoque_api.repository.ComputerRepository;
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

    @Transactional
    public ComputerResponseDTO createComputer(ComputerCreateDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com o ID: " + dto.getCategoryId()));

        Supplier supplier = null;
        if (dto.getSupplierId() != null) {
            supplier = supplierRepository.findById(dto.getSupplierId())
                    .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado com o ID: " + dto.getSupplierId()));
        }

        Computer newComputer = new Computer();
        newComputer.setAssetTag(dto.getAssetTag());
        newComputer.setPurchaseDate(dto.getPurchaseDate());
        newComputer.setStatus(dto.getStatus());
        newComputer.setNotes(dto.getNotes());
        newComputer.setSupplier(supplier);
        newComputer.setName(dto.getName());
        newComputer.setSerialNumber(dto.getSerialNumber());
        newComputer.setCpu(dto.getCpu());
        newComputer.setRamSizeInGB(dto.getRamSizeInGB());
        newComputer.setStorageSizeInGB(dto.getStorageSizeInGB());
        newComputer.setOs(dto.getOs());
        newComputer.setCategory(category);

        Computer savedComputer = computerRepository.save(newComputer);
        return toResponseDTO(savedComputer);
    }

    @Transactional(readOnly = true)
    public List<ComputerResponseDTO> findAllComputers() {
        return computerRepository.findAllWithDetails()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ComputerResponseDTO> findComputerById(Long id) {
        return computerRepository.findByIdWithDetails(id)
                .map(this::toResponseDTO);
    }

    @Transactional
    public Optional<ComputerResponseDTO> updateComputer(Long id, ComputerUpdateDTO dto) {
        return computerRepository.findById(id).map(existingComputer -> {
            if (dto.getName() != null) existingComputer.setName(dto.getName());
            if (dto.getSerialNumber() != null) existingComputer.setSerialNumber(dto.getSerialNumber());
            if (dto.getCpu() != null) existingComputer.setCpu(dto.getCpu());
            if (dto.getRamSizeInGB() != null) existingComputer.setRamSizeInGB(dto.getRamSizeInGB());
            if (dto.getStorageSizeInGB() != null) existingComputer.setStorageSizeInGB(dto.getStorageSizeInGB());
            if (dto.getOs() != null) existingComputer.setOs(dto.getOs());
            if (dto.getAssetTag() != null) existingComputer.setAssetTag(dto.getAssetTag());
            if (dto.getPurchaseDate() != null) existingComputer.setPurchaseDate(dto.getPurchaseDate());
            if (dto.getStatus() != null) existingComputer.setStatus(dto.getStatus());
            if (dto.getNotes() != null) existingComputer.setNotes(dto.getNotes());

            if (dto.getCategoryId() != null) {
                Category category = categoryRepository.findById(dto.getCategoryId())
                        .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada para atualização."));
                existingComputer.setCategory(category);
            }
            if (dto.getSupplierId() != null) {
                Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                        .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado para atualização."));
                existingComputer.setSupplier(supplier);
            }

            Computer updatedComputer = computerRepository.save(existingComputer);
            return toResponseDTO(updatedComputer);
        });
    }

    @Transactional
    public boolean deleteComputer(Long id) {
        if (computerRepository.existsById(id)) {
            computerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Converte uma entidade Computer para seu respectivo DTO de resposta de forma segura.
     * Este método substitui a necessidade do MapStruct e trata valores nulos.
     * @param computer A entidade a ser convertida.
     * @return O DTO de resposta populado.
     */
    private ComputerResponseDTO toResponseDTO(Computer computer) {
        // Verificação de segurança inicial
        if (computer == null) {
            return null;
        }

        // 1. Crie o DTO principal
        ComputerResponseDTO computerDto = new ComputerResponseDTO();

        // 2. Mapeie os campos simples e diretos
        computerDto.setId(computer.getId());
        computerDto.setAssetTag(computer.getAssetTag());
        computerDto.setPurchaseDate(computer.getPurchaseDate());
        computerDto.setStatus(computer.getStatus());
        computerDto.setNotes(computer.getNotes());
        computerDto.setName(computer.getName());
        computerDto.setSerialNumber(computer.getSerialNumber());
        computerDto.setCpu(computer.getCpu());
        computerDto.setRamSizeInGB(computer.getRamSizeInGB());
        computerDto.setStorageSizeInGB(computer.getStorageSizeInGB());
        computerDto.setOs(computer.getOs());

        // 3. Mapeie a Categoria (LÓGICA CORRIGIDA)
        if (computer.getCategory() != null) {
            // Crie o DTO da categoria APENAS SE a categoria existir
            CategoryResponseDTO categoryDto = new CategoryResponseDTO();
            categoryDto.setId(computer.getCategory().getId());
            categoryDto.setName(computer.getCategory().getName());
            computerDto.setCategory(categoryDto);
        } else {
            // Se não houver categoria, garanta que o campo no DTO principal seja null
            computerDto.setCategory(null);
        }

        // 4. Mapeie o Fornecedor (LÓGICA CORRIGIDA)
        if (computer.getSupplier() != null) {
            // Crie o DTO do fornecedor APENAS SE o fornecedor existir
            SupplierResponseDTO supplierDto = new SupplierResponseDTO();
            supplierDto.setId(computer.getSupplier().getId());
            supplierDto.setName(computer.getSupplier().getName());
            supplierDto.setCnpj(computer.getSupplier().getCnpj());
            supplierDto.setEmail(computer.getSupplier().getEmail());
            supplierDto.setPhone(computer.getSupplier().getPhone());
            computerDto.setSupplier(supplierDto);
        } else {
            // Se não houver fornecedor, garanta que o campo no DTO principal seja null
            computerDto.setSupplier(null);
        }

        // 5. Retorne o DTO principal completo
        return computerDto;
    }
}
