package com.matheuss.controle_estoque_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CollaboratorCreateDTO {
    @NotBlank(message = "O nome do colaborador é obrigatório.")
    private String name;

    @NotBlank(message = "O nome de usuário (matrícula/login) é obrigatório.")
    private String matricula;

    @NotBlank(message = "O departamento é obrigatório.")
    private String department;
}
