package com.matheuss.controle_estoque_api.service;

import com.matheuss.controle_estoque_api.domain.Category;
import com.matheuss.controle_estoque_api.dto.CategoryCreateDTO;
import com.matheuss.controle_estoque_api.dto.CategoryResponseDTO;
import com.matheuss.controle_estoque_api.dto.CategoryUpdateDTO;
import com.matheuss.controle_estoque_api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // CREATE
    @Transactional
    public CategoryResponseDTO createCategory(CategoryCreateDTO dto) {
        Category newCategory = new Category();
        newCategory.setName(dto.getName());
        Category savedCategory = categoryRepository.save(newCategory);
        return toResponseDTO(savedCategory);
    }

    // READ (ALL)
    @Transactional(readOnly = true)
    public List<CategoryResponseDTO> findAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    // READ (BY ID)
    @Transactional(readOnly = true)
    public Optional<CategoryResponseDTO> findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(this::toResponseDTO);
    }

    // UPDATE
    @Transactional
    public Optional<CategoryResponseDTO> updateCategory(Long id, CategoryUpdateDTO dto) {
        return categoryRepository.findById(id).map(existingCategory -> {
            existingCategory.setName(dto.getName());
            Category updatedCategory = categoryRepository.save(existingCategory);
            return toResponseDTO(updatedCategory);
        });
    }

    // DELETE
    @Transactional
    public boolean deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            // Futuramente, podemos adicionar uma verificação aqui para impedir a deleção
            // de uma categoria que está sendo usada por algum computador.
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Método auxiliar para converter a Entidade em DTO
    private CategoryResponseDTO toResponseDTO(Category category) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }
}
