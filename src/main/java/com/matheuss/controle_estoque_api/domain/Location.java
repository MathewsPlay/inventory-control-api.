package com.matheuss.controle_estoque_api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "location")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String paNumber;

    @Column(nullable = false)
    private String floor;

    @Column(nullable = false)
    private String sector;

    @Column(columnDefinition = "TEXT")
    private String description;

    @JsonIgnore
    // AQUI ESTÁ A ALTERAÇÃO
    @OneToMany(mappedBy = "location") 
    private List<Asset> assets;
}
