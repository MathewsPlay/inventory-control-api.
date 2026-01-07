package com.matheuss.controle_estoque_api.dto;

import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

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

    // Relacionamentos
    private CategoryResponseDTO category;
    private SupplierResponseDTO supplier;
    private LocationResponseDTO location;

    // --- NOVA LISTA DE COMPONENTES INTERNOS ---
    private List<ComponentResponseDTO> components;
}
