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

    /**
     * Busca todos os computadores, trazendo (FETCH) os relacionamentos
     * category e supplier na mesma consulta para evitar o problema N+1 e LazyInitializationException.
     * O 'LEFT JOIN' é usado para o supplier, pois ele pode ser nulo.
     */
    @NonNull
    @Query("SELECT c FROM Computer c JOIN FETCH c.category LEFT JOIN FETCH c.supplier")
    List<Computer> findAllWithDetails(); // <-- MÉTODO RENOMEADO

    /**
     * Busca um computador por ID, também trazendo os relacionamentos
     * na mesma consulta.
     */
    @Query("SELECT c FROM Computer c JOIN FETCH c.category LEFT JOIN FETCH c.supplier WHERE c.id = :id")
    Optional<Computer> findByIdWithDetails(@Param("id") Long id);
}
