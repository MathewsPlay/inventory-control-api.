package com.matheuss.controle_estoque_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.matheuss.controle_estoque_api.domain.Computer;

@Repository
public interface ComputerRepository extends JpaRepository<Computer, Long> {

    @NonNull
    @Query("SELECT c FROM Computer c JOIN FETCH c.category")
    List<Computer> findAllWithDetails(); // <-- MÉTODO RENOMEADO

    /**
     * Busca um computador por ID, também trazendo os relacionamentos
     * na mesma consulta.
     */
    @Query("SELECT c FROM Computer c JOIN FETCH c.category WHERE c.id = :id")
    Optional<Computer> findByIdWithDetails(@Param("id") Long id);
}
