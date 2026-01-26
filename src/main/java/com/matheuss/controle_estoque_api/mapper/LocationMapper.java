package com.matheuss.controle_estoque_api.mapper;

import com.matheuss.controle_estoque_api.domain.Location;
import com.matheuss.controle_estoque_api.dto.LocationCreateDTO;
import com.matheuss.controle_estoque_api.dto.LocationResponseDTO;
import com.matheuss.controle_estoque_api.dto.LocationUpdateDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assets", ignore = true)
    Location toEntity(LocationCreateDTO dto);

    LocationResponseDTO toResponseDTO(Location entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "assets", ignore = true)
    void updateEntityFromDto(LocationUpdateDTO dto, @MappingTarget Location entity);
}
