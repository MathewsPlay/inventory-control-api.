package com.matheuss.controle_estoque_api.service;

import com.matheuss.controle_estoque_api.domain.Category;
import com.matheuss.controle_estoque_api.dto.CategoryCreateDTO;
import com.matheuss.controle_estoque_api.dto.CategoryResponseDTO;
import com.matheuss.controle_estoque_api.dto.CategoryUpdateDTO;
import com.matheuss.controle_estoque_api.mapper.CategoryMapper;
import com.matheuss.controle_estoque_api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
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
    public Optional<CategoryResponseDTO> findCategoryById(Long id) {
        return categoryRepository.findById(id).map(categoryMapper::toResponseDTO);
    }

    @Transactional
    public Optional<CategoryResponseDTO> updateCategory(Long id, CategoryUpdateDTO dto) {
        return categoryRepository.findById(id).map(existing -> {
            existing.setName(dto.getName());
            return categoryMapper.toResponseDTO(categoryRepository.save(existing));
        });
    }

    @Transactional
    public boolean deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) return false;
        categoryRepository.deleteById(id);
        return true;
    }
}
