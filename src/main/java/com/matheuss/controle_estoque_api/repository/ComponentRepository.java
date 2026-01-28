package com.matheuss.controle_estoque_api.repository;

import com.matheuss.controle_estoque_api.domain.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Importar
import org.springframework.stereotype.Repository;

import java.util.List; // Importar

@Repository
public interface ComponentRepository extends JpaRepository<Component, Long> {

    // ====================================================================
    // == NOVO MÉTODO OTIMIZADO PARA EVITAR N+1 QUERIES ==
    // ====================================================================
    /**
     * Busca todos os componentes, trazendo em uma única consulta as entidades
     * relacionadas (Category, Location, Collaborator, Computer).
     * Isso evita o problema de N+1 queries.
     * @return Uma lista de Componentes com seus relacionamentos já carregados.
     */
    @Query("SELECT c FROM Component c " +
           "LEFT JOIN FETCH c.category " +
           "LEFT JOIN FETCH c.location " +
           "LEFT JOIN FETCH c.collaborator " +
           "LEFT JOIN FETCH c.computer")
    List<Component> findAllWithDetails();
}
