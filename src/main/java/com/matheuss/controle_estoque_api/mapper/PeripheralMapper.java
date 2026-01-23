package com.matheuss.controle_estoque_api.mapper;

import com.matheuss.controle_estoque_api.domain.Computer;
import com.matheuss.controle_estoque_api.domain.Peripheral;
import com.matheuss.controle_estoque_api.domain.User;
import com.matheuss.controle_estoque_api.dto.ComputerSimpleResponseDTO;
import com.matheuss.controle_estoque_api.dto.PeripheralCreateDTO;
import com.matheuss.controle_estoque_api.dto.PeripheralResponseDTO;
import com.matheuss.controle_estoque_api.dto.PeripheralUpdateDTO;
import com.matheuss.controle_estoque_api.dto.UserSimpleResponseDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy; // <<< IMPORT NECESSÁRIO

@Mapper(
    componentModel = "spring",
    uses = { ReferenceMapper.class },
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE // <<< CORREÇÃO FINAL
)
public interface PeripheralMapper {

    // --- MAPEAMENTO PARA ENTIDADE (CRIAÇÃO) ---
   
    @Mapping(source = "locationId", target = "location")
    @Mapping(source = "computerId", target = "computer")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Peripheral toEntity(PeripheralCreateDTO dto);

    // --- MAPEAMENTO PARA ENTIDADE (ATUALIZAÇÃO) ---
    
    @Mapping(source = "locationId", target = "location")
    @Mapping(source = "computerId", target = "computer")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(PeripheralUpdateDTO dto, @MappingTarget Peripheral peripheral);

    // --- MAPEAMENTO PARA DTO DE RESPOSTA ---
    PeripheralResponseDTO toResponseDTO(Peripheral peripheral);

    // --- MAPEAMENTO AUXILIAR ---
    ComputerSimpleResponseDTO toComputerSimpleDTO(Computer computer);

     
}
