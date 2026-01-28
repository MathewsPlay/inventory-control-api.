package com.matheuss.controle_estoque_api.service;

import com.matheuss.controle_estoque_api.domain.Category;
import com.matheuss.controle_estoque_api.dto.CategoryCreateDTO;
import com.matheuss.controle_estoque_api.dto.CategoryResponseDTO;
import com.matheuss.controle_estoque_api.dto.CategoryUpdateDTO;
import com.matheuss.controle_estoque_api.mapper.CategoryMapper;
// import com.matheuss.controle_estoque_api.repository.AssetRepository; // 1. REMOVER ESTE IMPORT
import com.matheuss.controle_estoque_api.repository.CategoryRepository;
import com.matheuss.controle_estoque_api.repository.ComponentRepository; // 2. ADICIONAR ESTE IMPORT
import com.matheuss.controle_estoque_api.repository.ComputerRepository;  // 3. ADICIONAR ESTE IMPORT
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    // private final AssetRepository assetRepository; // 4. REMOVER ESTA INJEÇÃO
    private final ComputerRepository computerRepository;  // 5. ADICIONAR ESTA INJEÇÃO
    private final ComponentRepository componentRepository; // 6. ADICIONAR ESTA INJEÇÃO
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponseDTO createCategory(CategoryCreateDTO dto) {
        Category newCategory = categoryMapper.toEntity(dto);
        Category saved = categoryRepository.save(newCategory);
        return categoryMapper.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryResponseDTO findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com o ID: " + id));
    }

    @Transactional
    public CategoryResponseDTO updateCategory(Long id, CategoryUpdateDTO dto) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com o ID: " + id));
        
        existing.setName(dto.getName());
        Category saved = categoryRepository.save(existing);
        return categoryMapper.toResponseDTO(saved);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com o ID: " + id));

        // ====================================================================
        // == VERIFICAÇÃO DE SEGURANÇA FINAL E CORRETA ==
        // ====================================================================
        // Verifica se a categoria está em uso por algum Computador OU por algum Componente.
        if (computerRepository.existsByCategoryId(id) || componentRepository.existsByCategoryId(id)) {
            throw new IllegalStateException("Não é possível deletar a categoria '" + category.getName() + "' pois ela está associada a um ou mais ativos.");
        }
        
        categoryRepository.deleteById(id);
    }
}
