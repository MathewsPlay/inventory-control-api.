package com.matheuss.controle_estoque_api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    /**
     * @Id: Define este campo como a chave primária (o identificador).
     * @GeneratedValue(strategy = GenerationType.IDENTITY): Configura o ID para ser
     * gerado automaticamente pelo banco de dados (auto-incremento).
     */
    @Id // <-- ESTA ANOTAÇÃO É A SOLUÇÃO PARA O ERRO
    @GeneratedValue(strategy = GenerationType.IDENTITY) // <-- E esta é para o auto-incremento
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
