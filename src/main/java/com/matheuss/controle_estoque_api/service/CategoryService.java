package com.matheuss.controle_estoque_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matheuss.controle_estoque_api.domain.Category;
import com.matheuss.controle_estoque_api.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Método para buscar todas as categorias.
     * @return uma lista de todas as categorias no banco.
     */
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    /**
     * Método para salvar (criar) uma nova categoria.
     * @param category O objeto da categoria a ser salvo.
     * @return A categoria salva (agora com um ID).
     */
    public Category save(Category category) {
        return categoryRepository.save(category);
    }
}
