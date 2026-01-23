package com.matheuss.controle_estoque_api.dto;

import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import com.matheuss.controle_estoque_api.domain.enums.EquipmentState; 
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime; 
import java.util.List;

@Data
public class ComputerResponseDTO {

    private Long id;
    private String assetTag;
    private AssetStatus status;
    private EquipmentState equipmentState;
    private LocalDate purchaseDate;
    private String notes;

    // DTOs aninhados para relacionamentos
    private LocationResponseDTO location;
    private CategoryResponseDTO category;
    private List<ComponentResponseDTO> components;

    // Campos específicos de Computer
    private String name;
    private String serialNumber;
    private String cpu;
    private int ramSizeInGB;
    private int storageSizeInGB;
    private String os;

    // Campos de Auditoria
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Usuário ao qual o ativo está alocado
    private UserSimpleResponseDTO user;

    // ====================================================================
    // == NOVO CAMPO: LISTA COM O HISTÓRICO DE EVENTOS DO ATIVO ==
    // ====================================================================
    private List<AssetHistoryResponseDTO> history;
}
