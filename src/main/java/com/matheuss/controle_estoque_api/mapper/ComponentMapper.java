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
    @Mapping(target = "category", ignore = true)  // service resolve por categoryId
    @Mapping(target = "location", ignore = true)  // service resolve por locationId
    @Mapping(target = "computer", ignore = true)  // service resolve por computerId
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "history", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Component toEntity(ComponentCreateDTO dto);

    ComponentResponseDTO toResponseDTO(Component entity);

    List<ComponentResponseDTO> toResponseDTOList(List<Component> list);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "computer", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "history", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(ComponentUpdateDTO dto, @MappingTarget Component entity);
}
