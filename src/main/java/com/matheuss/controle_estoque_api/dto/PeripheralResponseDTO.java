package com.matheuss.controle_estoque_api.dto;

import com.matheuss.controle_estoque_api.domain.User;
import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import com.matheuss.controle_estoque_api.dto.UserSimpleResponseDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PeripheralResponseDTO {
    // Campos herdados de Asset
    private Long id;
    private String assetTag;
    private AssetStatus status;
    private LocalDate purchaseDate;

    // Campos específicos de Peripheral
    private String type;
    private String name;
    private String model;
    private String serialNumber;

    // Relacionamentos como objetos completos
    private LocationResponseDTO location;
    private ComputerSimpleResponseDTO computer; // Usaremos um DTO simples para evitar loops

      private UserSimpleResponseDTO user;
}
