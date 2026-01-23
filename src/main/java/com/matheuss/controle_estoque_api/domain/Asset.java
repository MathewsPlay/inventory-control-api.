package com.matheuss.controle_estoque_api.domain;

import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import com.matheuss.controle_estoque_api.domain.enums.EquipmentState;
import com.matheuss.controle_estoque_api.domain.history.AssetHistory; // <-- IMPORT ADICIONADO
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList; // <-- IMPORT ADICIONADO
import java.util.List;      // <-- IMPORT ADICIONADO

@Entity
@Table(name = "asset")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "asset_type")
@Data
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public abstract class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String assetTag;

    @Column(nullable = false)
    private LocalDate purchaseDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentState equipmentState;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // --- CAMPOS DE AUDITORIA ---
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ====================================================================
    // == NOVA IMPLEMENTAÇÃO: RELACIONAMENTO COM O HISTÓRICO DE EVENTOS ==
    // Define o lado "um" do relacionamento "um-para-muitos" com AssetHistory.
    // mappedBy = "asset": Indica que a entidade AssetHistory é a dona do relacionamento
    //                     e possui o campo "asset" com a anotação @ManyToOne.
    // cascade = CascadeType.ALL: Garante que as operações (criar, deletar) no Asset
    //                            sejam propagadas para seus registros de histórico.
    // orphanRemoval = true: Se um registro de histórico for removido desta lista,
    //                       ele será deletado do banco de dados.
    // ====================================================================
    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AssetHistory> history = new ArrayList<>();
}
