package com.matheuss.controle_estoque_api.mapper;

import com.matheuss.controle_estoque_api.domain.history.AssetHistory;
import com.matheuss.controle_estoque_api.domain.history.HistoryEventType;
import com.matheuss.controle_estoque_api.dto.AssetHistoryResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CollaboratorMapper.class})
public interface AssetHistoryMapper {

    @Mapping(target = "eventType", source = "eventType")
    AssetHistoryResponseDTO toResponseDTO(AssetHistory h);

    List<AssetHistoryResponseDTO> toResponseDTOList(List<AssetHistory> list);

    default String map(HistoryEventType type) {
        return type != null ? type.getDescription() : null;
    }
}
