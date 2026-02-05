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
    
    private String assetTag;

    // Pode vir nulo (backend define EM_ESTOQUE)
    private AssetStatus status;

    @NotNull(message = "O estado do equipamento é obrigatório.")
    private EquipmentState equipmentState;

    @NotNull(message = "A data de compra é obrigatória.")
    private LocalDate purchaseDate;

    private Long locationId;

     @NotBlank(message = "O número de patrimônio é obrigatório para computadores.")
    private String patrimonio;

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

    // CAMPOS ESPECÍFICOS
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
