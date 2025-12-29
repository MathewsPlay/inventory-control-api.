package com.matheuss.controle_estoque_api.dto;

import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ComputerUpdateDTO {
    // Certifique-se de que todos estes campos existem no seu arquivo.
    // O importante é adicionar o locationId.
    private String assetTag;
    private AssetStatus status;
    private LocalDate purchaseDate;
    private Long supplierId;
    private Long categoryId;
    private Long locationId; // <-- ADICIONE ESTA LINHA

    private String name;
    private String serialNumber;
    private String cpu;
    private int ramSizeInGB;
    private int storageSizeInGB;
    private String os;
}
