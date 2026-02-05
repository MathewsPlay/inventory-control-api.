package com.matheuss.controle_estoque_api.domain;

import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import com.matheuss.controle_estoque_api.domain.enums.EquipmentState;
import com.matheuss.controle_estoque_api.domain.history.AssetHistory;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "asset")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "asset_type")
@Data
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
@Audited
public abstract class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true) 
    private String assetTag;

     @Column(unique = true)
    private String patrimonio;

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

    // =======================
    // CAMPOS ADMINISTRATIVOS
    // =======================
    private LocalDate dataRecebimento;
    private String chamadoCompra;
    private String sc;
    private String pedido;
    private String nf;
    private String centroCusto;

    // ===========================
    // CAMPOS DO CONTROLE (JIRA)
    // ===========================
    private String ticketJira;
    private String ticketDevolucaoJira;

    // ===========================
    // RELACIONAMENTOS
    // ===========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    // ====================================================================
    // == CORREÇÃO FINAL APLICADA SEM ALTERAR A LÓGICA ==
    // ====================================================================
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collaborator_id")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Collaborator collaborator;

    // ===========================
    // AUDITORIA (JPA)
    // ===========================
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ===========================
    // HISTÓRICO (DE EVENTOS DE NEGÓCIO)
    // ===========================
    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @NotAudited
    private List<AssetHistory> history = new ArrayList<>();
}
