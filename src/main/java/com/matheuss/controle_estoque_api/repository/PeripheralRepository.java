package com.matheuss.controle_estoque_api.repository;

import com.matheuss.controle_estoque_api.domain.Peripheral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Importar
import org.springframework.stereotype.Repository;

import java.util.List; // Importar

@Repository
public interface PeripheralRepository extends JpaRepository<Peripheral, Long> {

    // ====================================================================
    // == NOVO MÉTODO OTIMIZADO PARA EVITAR N+1 QUERIES ==
    // ====================================================================
    @Query("SELECT p FROM Peripheral p " +
           "LEFT JOIN FETCH p.location " +
           "LEFT JOIN FETCH p.collaborator " +
           "LEFT JOIN FETCH p.computer")
    List<Peripheral> findAllWithDetails();
}
