package com.matheuss.controle_estoque_api.dto;

import lombok.Data;

@Data
public class LocationCreateDTO {
    private String paNumber;
    private String floor;
    private String sector;
    private String description;
}
