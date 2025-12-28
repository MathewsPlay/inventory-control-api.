package com.matheuss.controle_estoque_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matheuss.controle_estoque_api.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // O corpo fica vazio! O Spring Data JPA cuida de tudo.
}
