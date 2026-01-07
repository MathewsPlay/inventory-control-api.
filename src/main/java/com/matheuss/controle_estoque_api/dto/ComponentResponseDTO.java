package com.matheuss.controle_estoque_api.dto;

import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ComponentResponseDTO {
    private Long id;
    private String assetTag;
    private AssetStatus status;
    private LocalDate purchaseDate;
    private String name;
    private String model;
    private String serialNumber;
    private CategoryResponseDTO category;
    private SupplierResponseDTO supplier;
    private LocationResponseDTO location;
  
}
