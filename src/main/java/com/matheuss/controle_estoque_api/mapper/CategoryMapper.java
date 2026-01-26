package com.matheuss.controle_estoque_api.mapper;

import com.matheuss.controle_estoque_api.domain.Category;
import com.matheuss.controle_estoque_api.dto.CategoryCreateDTO;
import com.matheuss.controle_estoque_api.dto.CategoryResponseDTO;
import com.matheuss.controle_estoque_api.dto.CategoryUpdateDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    Category toEntity(CategoryCreateDTO dto);

    CategoryResponseDTO toResponseDTO(Category entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(CategoryUpdateDTO dto, @MappingTarget Category entity);
}
