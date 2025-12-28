package com.matheuss.controle_estoque_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matheuss.controle_estoque_api.domain.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    
    // O corpo fica vazio! A mágica do Spring Data JPA acontece aqui.
}