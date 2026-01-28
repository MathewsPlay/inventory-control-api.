package com.matheuss.controle_estoque_api.controller;

import com.matheuss.controle_estoque_api.service.AssetAllocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assets" )
@RequiredArgsConstructor
public class AssetAllocationController {

    private final AssetAllocationService assetAllocationService;

    // Alocar para colaborador (Home office / empréstimo)
    // PATCH /api/assets/{assetId}/assign/{collaboratorId}
    @PatchMapping("/{assetId}/assign/{collaboratorId}")
    public ResponseEntity<Void> assignToCollaborator(
            @PathVariable Long assetId,
            @PathVariable Long collaboratorId
    ) {
        assetAllocationService.assignToCollaborator(assetId, collaboratorId);
        return ResponseEntity.ok().build();
    }

    // Alocar para localização (PA)
    // PATCH /api/assets/{assetId}/assign-location/{locationId}
    @PatchMapping("/{assetId}/assign-location/{locationId}")
    public ResponseEntity<Void> assignToLocation(
            @PathVariable Long assetId,
            @PathVariable Long locationId
    ) {
        assetAllocationService.assignToLocation(assetId, locationId);
        return ResponseEntity.ok().build();
    }

    // Devolver para estoque
    // PATCH /api/assets/{assetId}/unassign
    @PatchMapping("/{assetId}/unassign")
    public ResponseEntity<Void> unassignToStock(@PathVariable Long assetId) {
        assetAllocationService.unassignToStock(assetId);
        return ResponseEntity.ok().build();
    }

    // ====================================================================
    // == NOVO ENDPOINT PARA DESCARTE (SOFT DELETE) ==
    // ====================================================================
    /**
     * Marca um ativo como DESCARTADO (Soft Delete).
     * Esta operação é irreversível do ponto de vista do negócio e
     * só pode ser executada se o ativo não estiver em uso.
     * @param assetId O ID do ativo a ser descartado.
     * @return ResponseEntity com status 200 OK.
     */
    @PatchMapping("/{assetId}/dispose")
    public ResponseEntity<Void> disposeAsset(@PathVariable Long assetId) {
        assetAllocationService.disposeAsset(assetId);
        return ResponseEntity.ok().build();
    }
}
