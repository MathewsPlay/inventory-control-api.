package com.matheuss.controle_estoque_api.dto;

import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import com.matheuss.controle_estoque_api.domain.enums.EquipmentState; // <<< IMPORT ADICIONADO
import lombok.Data;
import java.time.LocalDate;

@Data
public class ComputerUpdateDTO {
    // Campos que podem ser atualizados
    private String assetTag;
    private AssetStatus status;
    private LocalDate purchaseDate;
    private Long categoryId;
    private Long locationId;

    private String name;
    private String serialNumber;
    private String cpu;
    private Integer ramSizeInGB; // Usar Integer para permitir nulo em updates
    private Integer storageSizeInGB; // Usar Integer para permitir nulo em updates
    private String os;

    // --- CAMPOS ADICIONADOS PARA CORRIGIR OS AVISOS ---
    private EquipmentState equipmentState;
    private String notes;
}
