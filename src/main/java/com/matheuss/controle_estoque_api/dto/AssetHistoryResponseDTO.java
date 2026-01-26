package com.matheuss.controle_estoque_api.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AssetHistoryResponseDTO {
    private Long id;
    private String eventType;
    private String details;
    private LocalDateTime eventDate;
    private CollaboratorSimpleResponseDTO associatedUser;
}
