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

    // ====================================================================
    // == NOVOS MÉTODOS PARA VALIDAÇÃO DE UNICIDADE ==
    // ====================================================================

    /**
     * Verifica de forma otimizada se já existe um ativo com o número de patrimônio informado.
     * @param patrimonio O número de patrimônio a ser verificado.
     * @return true se o número de patrimônio já estiver em uso, false caso contrário.
     */
    boolean existsByPatrimonio(String patrimonio);

    /**
     * Verifica de forma otimizada se já existe um ativo com o Asset Tag informado.
     * Este método é mais explícito que o findByAssetTag().isPresent() e pode ser mais performático.
     * @param assetTag O Asset Tag a ser verificado.
     * @return true se o Asset Tag já estiver em uso, false caso contrário.
     */
    boolean existsByAssetTag(String assetTag);

}
