package com.matheuss.controle_estoque_api.service;

import com.matheuss.controle_estoque_api.domain.Asset;
import com.matheuss.controle_estoque_api.domain.Location;
import com.matheuss.controle_estoque_api.domain.Collaborator;
import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import com.matheuss.controle_estoque_api.domain.history.HistoryEventType;
import com.matheuss.controle_estoque_api.repository.AssetRepository;
import com.matheuss.controle_estoque_api.repository.LocationRepository;
import com.matheuss.controle_estoque_api.repository.CollaboratorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AssetAllocationService {

    private final AssetRepository assetRepository;
    private final CollaboratorRepository userRepository;
    private final LocationRepository locationRepository;
    private final AssetHistoryService assetHistoryService;

    // =========================
    // Commands
    // =========================

    @Transactional
    public void assignToCollaborator(Long assetId, Long collaboratorId) {
        Asset asset = requireAsset(assetId);
        Collaborator collaborator = requireCollaborator(collaboratorId);

        validateCanAssignFromStock(asset);

        asset.setUser(collaborator);
        asset.setLocation(null);
        asset.setStatus(AssetStatus.EM_USO);

        recordAllocation(asset,
                "Ativo alocado para o colaborador: " + collaborator.getName() + " (ID: " + collaborator.getId() + ").",
                collaborator
        );
    }

    @Transactional
    public void assignToLocation(Long assetId, Long locationId) {
        Asset asset = requireAsset(assetId);
        Location location = requireLocation(locationId);

        validateCanAssignFromStock(asset);

        asset.setUser(null);
        asset.setLocation(location);
        asset.setStatus(AssetStatus.EM_USO);

        recordAllocation(asset,
                "Ativo alocado para a localização: PA " + location.getPaNumber() + " (" + location.getSector() + ").",
                null
        );
    }

    @Transactional
    public void unassignToStock(Long assetId) {
        Asset asset = requireAsset(assetId);

        validateCanUnassignToStock(asset);

        Collaborator previousUser = asset.getUser();
        Location previousLocation = asset.getLocation();

        asset.setUser(null);
        asset.setLocation(null);
        asset.setStatus(AssetStatus.EM_ESTOQUE);

        recordReturn(asset, previousUser, previousLocation);
    }

    // =========================
    // Helpers (Business rules)
    // =========================

    private void validateCanAssignFromStock(Asset asset) {
        if (asset.getStatus() != AssetStatus.EM_ESTOQUE) {
            throw new IllegalStateException(
                    "Operação não permitida: o ativo '" + asset.getAssetTag() + "' não está em estoque."
            );
        }
        if (asset.getUser() != null || asset.getLocation() != null) {
            throw new IllegalStateException("Operação não permitida: o ativo já está alocado.");
        }
    }

    private void validateCanUnassignToStock(Asset asset) {
        if (asset.getStatus() != AssetStatus.EM_USO) {
            throw new IllegalStateException("Operação não permitida: o ativo não está em uso para ser devolvido.");
        }
        if (asset.getUser() == null && asset.getLocation() == null) {
            throw new IllegalStateException("Operação não permitida: o ativo não está alocado a ninguém/nenhuma PA.");
        }
    }

    private void recordAllocation(Asset asset, String details, Collaborator associatedUser) {
        assetHistoryService.registerEvent(asset, HistoryEventType.ALOCACAO, details, associatedUser);
    }

    private void recordReturn(Asset asset, Collaborator previousUser, Location previousLocation) {
        String details;
        if (previousUser != null) {
            details = "Ativo devolvido ao estoque. Estava anteriormente com o colaborador: "
                    + previousUser.getName() + " (ID: " + previousUser.getId() + ").";
        } else if (previousLocation != null) {
            details = "Ativo devolvido ao estoque. Estava anteriormente na localização: PA "
                    + previousLocation.getPaNumber() + " (" + previousLocation.getSector() + ").";
        } else {
            details = "Ativo devolvido ao estoque.";
        }

        assetHistoryService.registerEvent(asset, HistoryEventType.DEVOLUCAO, details, previousUser);
    }

    // =========================
    // Helpers (Entity loading)
    // =========================

    private Asset requireAsset(Long id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ativo não encontrado com o ID: " + id));
    }

    private Collaborator requireCollaborator(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Colaborador não encontrado com o ID: " + id));
    }

    private Location requireLocation(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Localização não encontrada com o ID: " + id));
    }
}
