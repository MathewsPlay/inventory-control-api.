package com.matheuss.controle_estoque_api.dto;

import lombok.Data;
import java.util.List;

@Data
public class CollaboratorResponseDTO {
    private Long id;
    private String name;
    private String matricula;
    private String department;
    // Usaremos um DTO simples para os ativos para não expor dados demais
    private List<AssetSimpleResponseDTO> assets;
}
