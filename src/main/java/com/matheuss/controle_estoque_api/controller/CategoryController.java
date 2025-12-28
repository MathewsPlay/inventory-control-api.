package com.matheuss.controle_estoque_api.controller;

import com.matheuss.controle_estoque_api.domain.Category;
import com.matheuss.controle_estoque_api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categories" )
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Endpoint para LISTAR TODAS as categorias.
     * Mapeia requisições HTTP GET para /categories.
     */
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    /**
     * Endpoint para CRIAR UMA NOVA categoria.
     * Mapeia requisições HTTP POST para /categories.
     */
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        // 1. Salva a nova categoria usando o serviço.
        Category savedCategory = categoryService.save(category);
        
        // 2. Cria a URL para o novo recurso (ex: /categories/1).
        //    AQUI ESTAVA O ERRO: Usamos a variável 'savedCategory' e chamamos o método '.getId()' nela.
        URI location = URI.create("/categories/" + savedCategory.getId());
        
        // 3. Retorna a resposta 201 Created com a localização e o objeto salvo.
        return ResponseEntity.created(location).body(savedCategory);
    }
}
