package com.matheuss.controle_estoque_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SwapComponentRequestDTO {

    @NotNull(message = "O ID do componente a ser desinstalado é obrigatório.")
    private Long componentToUninstallId;

    @NotNull(message = "O ID do componente a ser instalado é obrigatório.")
    private Long componentToInstallId;
}
