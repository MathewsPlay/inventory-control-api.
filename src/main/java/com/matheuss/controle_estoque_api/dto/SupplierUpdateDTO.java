package com.matheuss.controle_estoque_api.dto;

import lombok.Data;

@Data
public class SupplierUpdateDTO {
    private String name;
    private String cnpj;
    private String email;
    private String phone;
}
