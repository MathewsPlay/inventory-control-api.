package com.matheuss.controle_estoque_api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
    private Category category;
    @OneToMany(mappedBy = "computer", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
private List<Component> components = new ArrayList<>();

}
