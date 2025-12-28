package com.matheuss.controle_estoque_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matheuss.controle_estoque_api.domain.Asset;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    // Exemplo de método de busca customizado que podemos adicionar no futuro.
    // O Spring Data JPA implementa este método automaticamente baseado no nome.
    
    Optional<Asset> findByAssetTag(String assetTag);

}
