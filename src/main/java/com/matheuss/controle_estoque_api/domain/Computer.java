package com.matheuss.controle_estoque_api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited; // Importar se necessário (já vem de Asset)
import org.hibernate.envers.NotAudited; // 1. IMPORTAR
import org.hibernate.envers.RelationTargetAuditMode; // Importar

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("COMPUTER")
@Data
@EqualsAndHashCode(callSuper = true)
public class Computer extends Asset {

    private String name;
    private String serialNumber;
    private String cpu;
    private int ramSizeInGB;
    private int storageSizeInGB;
    private String os;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    // CORREÇÃO QUE JÁ FIZEMOS ANTES (garantindo que está aqui)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Category category;

    // --- RELACIONAMENTO BIDIRECIONAL (APENAS COMPONENTES) ---
    @OneToMany(mappedBy = "computer", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    @JsonIgnore // Mantido para evitar loops na serialização JSON
    // ====================================================================
    // == CORREÇÃO FINAL PARA O ERRO DE INICIALIZAÇÃO DO ENVERS ==
    // ====================================================================
    @NotAudited // 2. ADICIONAR ESTA ANOTAÇÃO
    private List<Component> components = new ArrayList<>();
}
