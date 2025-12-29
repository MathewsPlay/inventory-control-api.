package com.matheuss.controle_estoque_api.dto;

import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ComputerResponseDTO {
    private Long id;
    private String assetTag;
    private AssetStatus status;
    private LocalDate purchaseDate;
    private String name;
    private String serialNumber;
    private String cpu;
    private int ramSizeInGB;
    private int storageSizeInGB;
    private String os;

    // DTOs aninhados para os relacionamentos
    private CategoryResponseDTO category;
    private SupplierResponseDTO supplier;
    private LocationResponseDTO location; // <-- CAMPO NOVO
}
