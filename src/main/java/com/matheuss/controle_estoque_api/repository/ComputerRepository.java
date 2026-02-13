package com.matheuss.controle_estoque_api.repository;

import com.matheuss.controle_estoque_api.domain.Computer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; 
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface ComputerRepository extends JpaRepository<Computer, Long>, JpaSpecificationExecutor<Computer> {

   
    @NonNull
    @Query("SELECT c FROM Computer c LEFT JOIN FETCH c.category")
    List<Computer> findAllWithDetails();

   
    @Query("SELECT c FROM Computer c LEFT JOIN FETCH c.category WHERE c.id = :id")
    Optional<Computer> findByIdWithDetails(@Param("id") Long id);

    
    /**
     * Verifica se existe algum computador associado a uma determinada categoria.
     * @param categoryId O ID da categoria a ser verificada.
     * @return true se pelo menos um computador usar a categoria, false caso contrário.
     */
    boolean existsByCategoryId(Long categoryId);
}
