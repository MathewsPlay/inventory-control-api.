package com.matheuss.controle_estoque_api.dto;

import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import com.matheuss.controle_estoque_api.domain.enums.EquipmentState;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ComponentUpdateDTO {

    private String assetTag;
    private String patrimonio;
    private AssetStatus status;
    private LocalDate purchaseDate;
    private Long locationId;

    private String name;
    private String model;
    private String serialNumber;
    private Long collaboratorId;
    private Long categoryId;
    private Long computerId;


    private EquipmentState equipmentState;
    private String notes;

    // CAMPOS ADMINISTRATIVOS (EXCEL)
    private LocalDate dataRecebimento;
    private String chamadoCompra;
    private String sc;
    private String pedido;
    private String nf;
    private String centroCusto;

    // JIRA (CONTROLE)
    private String ticketJira;
    private String ticketDevolucaoJira;
}
