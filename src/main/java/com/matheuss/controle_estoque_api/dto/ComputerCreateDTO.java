package com.matheuss.controle_estoque_api.dto;

import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import com.matheuss.controle_estoque_api.domain.enums.EquipmentState; 
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ComputerCreateDTO {

    @NotBlank(message = "A etiqueta do ativo (assetTag) é obrigatória.")
    private String assetTag;

    @NotNull(message = "O status do ativo é obrigatório.")
    private AssetStatus status;

    @NotNull(message = "O estado do equipamento é obrigatório.") // 2. Adicione o novo campo com validação
    private EquipmentState equipmentState;

    @NotNull(message = "A data de compra é obrigatória.")
    private LocalDate purchaseDate;

    private Long locationId;

    private String notes;

    // Campos específicos de Computer
    @NotBlank(message = "O nome do computador é obrigatório.")
    private String name;

    @NotBlank(message = "O número de série é obrigatório.")
    private String serialNumber;

    private String cpu;

    @Positive(message = "A quantidade de RAM deve ser um número positivo.")
    private int ramSizeInGB;

    @Positive(message = "O tamanho do armazenamento deve ser um número positivo.")
    private int storageSizeInGB;

    private String os;

    @NotNull(message = "O ID da categoria é obrigatório.")
    private Long categoryId;
}
