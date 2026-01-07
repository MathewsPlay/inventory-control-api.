package com.matheuss.controle_estoque_api.mapper;

import com.matheuss.controle_estoque_api.domain.Computer;
import com.matheuss.controle_estoque_api.dto.ComputerCreateDTO;
import com.matheuss.controle_estoque_api.dto.ComputerResponseDTO;
import com.matheuss.controle_estoque_api.dto.ComputerUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {
    ReferenceMapper.class,
    ComponentMapper.class // Essencial para o mapeamento da lista
})
public interface ComputerMapper {

    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "supplierId", target = "supplier")
    @Mapping(source = "locationId", target = "location")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "notes", ignore = true)
    @Mapping(target = "components", ignore = true)
    Computer toEntity(ComputerCreateDTO dto);

    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "supplierId", target = "supplier")
    @Mapping(source = "locationId", target = "location")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "notes", ignore = true)
    @Mapping(target = "components", ignore = true)
    void updateEntityFromDto(ComputerUpdateDTO dto, @MappingTarget Computer computer);

    // --- CORREÇÃO PRINCIPAL AQUI ---
    // Adicionamos um @Mapping explícito para a lista de componentes.
    // Isso força o MapStruct a usar o ComponentMapper para converter cada item.
    @Mapping(source = "components", target = "components")
    ComputerResponseDTO toResponseDTO(Computer computer);
}
