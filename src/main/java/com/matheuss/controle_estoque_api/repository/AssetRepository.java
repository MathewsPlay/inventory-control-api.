package com.matheuss.controle_estoque_api.repository;

import com.matheuss.controle_estoque_api.domain.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    /**
     * Busca um ativo pela sua etiqueta (assetTag), que deve ser única.
     * @param assetTag A etiqueta do ativo a ser buscada.
     * @return Um Optional contendo o ativo se encontrado.
     */
    Optional<Asset> findByAssetTag(String assetTag);

    // O método 'boolean existsByCategoryId(Long categoryId);' foi removido daqui
    // porque a entidade 'Asset' não possui a propriedade 'categoryId'.
}
