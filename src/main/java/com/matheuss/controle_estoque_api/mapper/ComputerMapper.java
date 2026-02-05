package com.matheuss.controle_estoque_api.mapper;

import com.matheuss.controle_estoque_api.domain.Computer;
import com.matheuss.controle_estoque_api.dto.ComputerCreateDTO;
import com.matheuss.controle_estoque_api.dto.ComputerResponseDTO;
import com.matheuss.controle_estoque_api.dto.ComputerUpdateDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                CategoryMapper.class,
                LocationMapper.class,
                CollaboratorMapper.class,
                AssetHistoryMapper.class,
                ComponentMapper.class
        }
)
public interface ComputerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "collaborator", ignore = true)
    @Mapping(target = "history", ignore = true)
    @Mapping(target = "components", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Computer toEntity(ComputerCreateDTO dto);

    ComputerResponseDTO toResponseDTO(Computer entity);

    List<ComputerResponseDTO> toResponseDTOList(List<Computer> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true) // Boa prática adicionar o id também
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "collaborator", ignore = true)
    
    @Mapping(target = "status", ignore = true) // Impede que o Mapper sobrescreva a lógica de status do Service.
    @Mapping(target = "history", ignore = true)
    @Mapping(target = "components", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(ComputerUpdateDTO dto, @MappingTarget Computer entity);
}
