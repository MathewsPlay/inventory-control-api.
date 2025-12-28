package com.matheuss.controle_estoque_api.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
    private Category category;
}
