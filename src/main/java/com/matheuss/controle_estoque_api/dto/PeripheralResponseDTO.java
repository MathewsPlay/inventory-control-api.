package com.matheuss.controle_estoque_api.dto;

import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import com.matheuss.controle_estoque_api.domain.enums.EquipmentState;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PeripheralResponseDTO {

    private Long id;
    private String assetTag;
    private AssetStatus status;
    private EquipmentState equipmentState;
    private LocalDate purchaseDate;
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

    private String type;
    private String name;
    private String model;
    private String serialNumber;

    private LocationResponseDTO location;
    private ComputerSimpleResponseDTO computer;
    private CollaboratorSimpleResponseDTO user;
}
