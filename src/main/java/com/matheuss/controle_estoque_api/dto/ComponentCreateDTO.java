package com.matheuss.controle_estoque_api.dto;

import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import com.matheuss.controle_estoque_api.domain.enums.EquipmentState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ComponentCreateDTO {

    // --- Campos herdados de Asset ---
    @NotBlank(message = "A etiqueta do ativo (assetTag) é obrigatória.")
    private String assetTag;

    @NotNull(message = "O status do ativo é obrigatório.")
    private AssetStatus status;

    @NotNull(message = "O estado do equipamento é obrigatório.")
    private EquipmentState equipmentState; // <<< CAMPO ADICIONADO

    @NotNull(message = "A data de compra é obrigatória.")
    private LocalDate purchaseDate;

    private Long locationId; // Onde o componente está em estoque

    private String notes; // <<< CAMPO ADICIONADO

    // --- Campos específicos de Component ---
    @NotBlank(message = "O nome do componente é obrigatório.")
    private String name; // Ex: "Corsair Vengeance LPX"

    private String model;

    @NotBlank(message = "O número de série é obrigatório.")
    private String serialNumber;

    // --- Relacionamentos ---
    @NotNull(message = "O ID da categoria é obrigatório.")
    private Long categoryId; // Categoria do componente (RAM, SSD, etc.)

    private Long computerId; // Opcional: ID do computador onde será instalado imediatamente
}
