package com.matheuss.controle_estoque_api.dto;

// Import corrigido para usar o enum unificado
import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ComputerCreateDTO {
    private String assetTag;
    private LocalDate purchaseDate;
    
    // Campo corrigido para usar o tipo AssetStatus
    private AssetStatus status; 
    
    private String notes;
    private Long supplierId;
    private String name;
    private String serialNumber;
    private String cpu;
    private Integer ramSizeInGB;
    private Integer storageSizeInGB;
    private String os;
    private Long categoryId;
}
