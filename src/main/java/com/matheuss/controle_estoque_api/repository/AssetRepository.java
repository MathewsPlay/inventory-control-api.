package com.matheuss.controle_estoque_api.repository;

import com.matheuss.controle_estoque_api.domain.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    Optional<Asset> findByAssetTag(String assetTag);
    /**
     * Verifica de forma otimizada se existe algum ativo associado a uma determinada categoria.
     * O Spring Data JPA cria a consulta SQL (ex: SELECT 1 FROM asset WHERE category_id = ? LIMIT 1)
     * que é muito mais performática do que buscar todos os ativos e verificar se a lista está vazia.
     * @param categoryId O ID da categoria a ser verificada.
     * @return true se pelo menos um ativo usar a categoria, false caso contrário.
     */
    boolean existsByCategoryId(Long categoryId);

}
