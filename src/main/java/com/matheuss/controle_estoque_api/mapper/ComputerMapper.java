package com.matheuss.controle_estoque_api.mapper;

import com.matheuss.controle_estoque_api.domain.Computer;
import com.matheuss.controle_estoque_api.dto.ComputerCreateDTO;
import com.matheuss.controle_estoque_api.dto.ComputerResponseDTO;
import com.matheuss.controle_estoque_api.dto.ComputerUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    uses = { ReferenceMapper.class, ComponentMapper.class, AssetHistoryMapper.class },
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ComputerMapper {

    
    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "locationId", target = "location")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "components", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "history", ignore = true)
    // ====================================================================
    // == CORREÇÃO: Ignorando explicitamente o campo 'user' ==
    // ====================================================================
    @Mapping(target = "user", ignore = true)
    Computer toEntity(ComputerCreateDTO dto);
  
   
    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "locationId", target = "location")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "components", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "history", ignore = true)
    // ====================================================================
    // == CORREÇÃO: Ignorando explicitamente o campo 'user' ==
    // ====================================================================
    @Mapping(target = "user", ignore = true)
    void updateEntityFromDto(ComputerUpdateDTO dto, @MappingTarget Computer computer);

    @Mapping(source = "components", target = "components")
    @Mapping(source = "history", target = "history")
    ComputerResponseDTO toResponseDTO(Computer computer);
}
