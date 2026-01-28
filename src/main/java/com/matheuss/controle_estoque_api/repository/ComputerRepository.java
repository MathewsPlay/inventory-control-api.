package com.matheuss.controle_estoque_api.repository;

import com.matheuss.controle_estoque_api.domain.Computer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComputerRepository extends JpaRepository<Computer, Long> {

    @NonNull
    @Query("SELECT c FROM Computer c LEFT JOIN FETCH c.category") // Pequena correção: LEFT JOIN é mais seguro
    List<Computer> findAllWithDetails();

    @Query("SELECT c FROM Computer c LEFT JOIN FETCH c.category WHERE c.id = :id") // Pequena correção: LEFT JOIN é mais seguro
    Optional<Computer> findByIdWithDetails(@Param("id") Long id);

    // ====================================================================
    // == MÉTODO ADICIONADO PARA A VERIFICAÇÃO DE SEGURANÇA ==
    // ====================================================================
    /**
     * Verifica se existe algum computador associado a uma determinada categoria.
     * @param categoryId O ID da categoria a ser verificada.
     * @return true se pelo menos um computador usar a categoria, false caso contrário.
     */
    boolean existsByCategoryId(Long categoryId);
}
