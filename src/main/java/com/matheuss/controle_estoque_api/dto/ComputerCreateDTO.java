package com.matheuss.controle_estoque_api.dto;

import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ComputerCreateDTO {
    private String assetTag;
    private AssetStatus status;
    private LocalDate purchaseDate;
    private Long supplierId;
    private Long categoryId;
    private Long locationId; // <-- CAMPO NOVO

    private String name;
    private String serialNumber;
    private String cpu;
    private int ramSizeInGB;
    private int storageSizeInGB;
    private String os;
}
