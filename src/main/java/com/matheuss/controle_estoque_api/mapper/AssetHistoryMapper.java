package com.matheuss.controle_estoque_api.mapper;

import com.matheuss.controle_estoque_api.domain.history.AssetHistory;
import com.matheuss.controle_estoque_api.dto.AssetHistoryResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { ReferenceMapper.class })
public interface AssetHistoryMapper {

    @Mapping(source = "eventType.description", target = "eventType")
    AssetHistoryResponseDTO toResponseDTO(AssetHistory assetHistory);

    List<AssetHistoryResponseDTO> toResponseDTOList(List<AssetHistory> assetHistoryList);
}
