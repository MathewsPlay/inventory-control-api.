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

    // Campos específicos de um componente
    @Column(name = "component_name")
    private String name; // Ex: "Corsair Vengeance LPX", "Kingston A400"

    private String model;
    private String serialNumber;

    // Relacionamento com Categoria para definir o tipo (RAM, SSD, etc.)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // ====================================================================
    // == RELACIONAMENTO OPCIONAL COM COMPUTADOR ==
    // Um componente pode estar em estoque (não instalado) ou instalado em um computador.
    // A ausência de 'nullable = false' na @JoinColumn torna a coluna anulável por padrão.
    // ====================================================================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "computer_id")
    private Computer computer;
    
   @Column(name = "component_type") 
    private String type;
}
