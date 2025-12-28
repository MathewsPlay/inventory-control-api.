package com.matheuss.controle_estoque_api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("COMPONENT")
@Data
@EqualsAndHashCode(callSuper = true)
public class Component extends Asset {

    @Column(name = "component_name")
    private String name;
    
    private String type;
    private String specification;
    private int capacityInGB;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
