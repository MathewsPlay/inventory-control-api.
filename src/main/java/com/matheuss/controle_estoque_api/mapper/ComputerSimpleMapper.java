package com.matheuss.controle_estoque_api.mapper;

import com.matheuss.controle_estoque_api.domain.Computer;
import com.matheuss.controle_estoque_api.dto.ComputerSimpleResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComputerSimpleMapper {
    ComputerSimpleResponseDTO toSimpleResponseDTO(Computer computer);
}
