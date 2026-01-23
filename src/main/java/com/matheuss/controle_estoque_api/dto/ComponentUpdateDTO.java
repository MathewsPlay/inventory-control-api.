package com.matheuss.controle_estoque_api.dto;

import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import com.matheuss.controle_estoque_api.domain.enums.EquipmentState; // <<< IMPORT ADICIONADO
import lombok.Data;
import java.time.LocalDate;

@Data
public class ComponentUpdateDTO {
    // Campos que podem ser atualizados
    private String assetTag;
    private AssetStatus status;
    private LocalDate purchaseDate;
    private Long locationId;

    private String name;
    private String model;
    private String serialNumber;

    private Long categoryId;
    private Long computerId;

    // --- CAMPOS ADICIONADOS PARA CORRIGIR OS AVISOS ---
    private EquipmentState equipmentState;
    private String notes;
}
