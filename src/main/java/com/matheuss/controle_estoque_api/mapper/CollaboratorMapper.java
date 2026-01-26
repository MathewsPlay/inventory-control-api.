package com.matheuss.controle_estoque_api.mapper;

import com.matheuss.controle_estoque_api.domain.*;
import com.matheuss.controle_estoque_api.dto.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CollaboratorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "assets", ignore = true)
    Collaborator toEntity(CollaboratorCreateDTO dto);

    CollaboratorResponseDTO toResponseDTO(Collaborator entity);

    CollaboratorSimpleResponseDTO toSimpleResponseDTO(Collaborator entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "assets", ignore = true)
    void updateEntityFromDto(CollaboratorUpdateDTO dto, @MappingTarget Collaborator entity);

    // ===== Assets dentro de UserResponseDTO =====
    @Mapping(target = "name", expression = "java(resolveAssetName(asset))")
    AssetSimpleResponseDTO toAssetSimpleResponseDTO(Asset asset);

    List<AssetSimpleResponseDTO> toAssetSimpleResponseDTOList(List<Asset> assets);

    default String resolveAssetName(Asset asset) {
        if (asset == null) return null;

        if (asset instanceof Computer) {
            return ((Computer) asset).getName();
        }
        if (asset instanceof Peripheral) {
            return ((Peripheral) asset).getName();
        }
        if (asset instanceof Component) {
            return ((Component) asset).getName();
        }
        return null;
    }
}
