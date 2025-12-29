package com.matheuss.controle_estoque_api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "locations") // Nome da tabela no banco
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String paNumber; // O número da PA. Ex: "7", "8", "60"

    @Column(nullable = false)
    private String floor; // O andar. Ex: "7º Andar", "Térreo"

    @Column(nullable = false)
    private String sector; // O setor/cliente. Ex: "iFood", "Bancos"

    @Column(columnDefinition = "TEXT")
    private String description; // Campo opcional para detalhes. Ex: "Mesa ao lado do pilar"

    @JsonIgnore // Essencial para evitar loops infinitos ao serializar
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Asset> assets; // Lista de todos os ativos (computadores, mouses, etc.) nesta PA
}
