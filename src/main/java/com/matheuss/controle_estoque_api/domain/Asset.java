package com.matheuss.controle_estoque_api.domain;

// Imports do Jakarta Persistence
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;

// Imports do Lombok
import lombok.Data;
import lombok.EqualsAndHashCode;

// ESTE É O IMPORT QUE FALTA
import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "asset_type")
@Data
@EqualsAndHashCode(of = "id")
public abstract class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String assetTag; // Etiqueta do ativo (ex: "NTB-00123")

    private LocalDate purchaseDate; // Data da compra

    @Enumerated(EnumType.STRING) // Diz ao JPA para salvar o NOME do enum (ex: "EM_USO") no banco
    private AssetStatus status; // O campo que usa o enum

    @Column(columnDefinition = "TEXT")
    private String notes; // Observações

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier; // Fornecedor

    @ManyToOne(fetch = FetchType.LAZY) // LAZY é bom para performance
    @JoinColumn(name = "location_id") // Nome da coluna de chave estrangeira na tabela 'assets'
     private Location location;

}
