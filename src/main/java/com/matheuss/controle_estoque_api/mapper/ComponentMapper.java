package com.matheuss.controle_estoque_api.mapper;

import com.matheuss.controle_estoque_api.domain.Component;
import com.matheuss.controle_estoque_api.dto.ComponentCreateDTO;
import com.matheuss.controle_estoque_api.dto.ComponentResponseDTO;
import com.matheuss.controle_estoque_api.dto.ComponentUpdateDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {CategoryMapper.class, LocationMapper.class, CollaboratorMapper.class}
)
public interface ComponentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "computer", ignore = true)
    @Mapping(target = "collaborator", ignore = true)
    @Mapping(target = "history", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Component toEntity(ComponentCreateDTO dto);

    ComponentResponseDTO toResponseDTO(Component entity);

    List<ComponentResponseDTO> toResponseDTOList(List<Component> list);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true) // Boa prática
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "computer", ignore = true)
    @Mapping(target = "collaborator", ignore = true)
    // ====================================================================
    // == LINHA CRÍTICA ADICIONADA ==
    // ====================================================================
    @Mapping(target = "status", ignore = true) // Impede que o Mapper sobrescreva a lógica de status do Service.
    @Mapping(target = "history", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(ComponentUpdateDTO dto, @MappingTarget Component entity);
}
