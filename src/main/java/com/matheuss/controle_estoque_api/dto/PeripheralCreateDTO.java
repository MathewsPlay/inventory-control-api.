package com.matheuss.controle_estoque_api.dto;

import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import com.matheuss.controle_estoque_api.domain.enums.EquipmentState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PeripheralCreateDTO {

    // --- Campos herdados de Asset ---
    @NotBlank(message = "A etiqueta do ativo (assetTag) é obrigatória.")
    private String assetTag;

    @NotNull(message = "O status do ativo é obrigatório.")
    private AssetStatus status;

    @NotNull(message = "O estado do equipamento é obrigatório.")
    private EquipmentState equipmentState; // <<< CAMPO ADICIONADO

    @NotNull(message = "A data de compra é obrigatória.")
    private LocalDate purchaseDate;

    private Long locationId;

    private String notes; // <<< CAMPO ADICIONADO

    // --- Campos específicos de Peripheral ---
    @NotBlank(message = "O tipo do periférico é obrigatório.")
    private String type; // Ex: "Mouse", "Teclado", "Monitor"

    @NotBlank(message = "O nome do periférico é obrigatório.")
    private String name;

    private String model;

    @NotBlank(message = "O número de série é obrigatório.")
    private String serialNumber;

    // --- Relacionamento opcional com Computer ---
    private Long computerId;
}
