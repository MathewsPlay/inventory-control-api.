package com.matheuss.controle_estoque_api.repository;

import com.matheuss.controle_estoque_api.domain.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {

    // Nesta consulta, o apelido para Collaborator é 'c', então está correta.
    @Query("select distinct c from Collaborator c left join fetch c.assets")
    List<Collaborator> findAllWithAssets();

    // ====================================================================
    // == CORREÇÃO DO ERRO DE DIGITAÇÃO NA CONSULTA ==
    // ====================================================================
    // A consulta original usava 'u' e 'c' misturados. A versão correta usa apenas 'u'.
    @Query("select u from Collaborator u left join fetch u.assets where u.id = :id")
    Optional<Collaborator> findByIdWithAssets(@Param("id") Long id);
}
