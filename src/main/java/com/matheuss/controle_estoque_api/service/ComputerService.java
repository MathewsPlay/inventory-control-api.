package com.matheuss.controle_estoque_api.service;

import com.matheuss.controle_estoque_api.domain.Collaborator;
import com.matheuss.controle_estoque_api.domain.Component;
import com.matheuss.controle_estoque_api.domain.Computer;
import com.matheuss.controle_estoque_api.domain.Location;
import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import com.matheuss.controle_estoque_api.domain.history.HistoryEventType;
import com.matheuss.controle_estoque_api.dto.ComputerCreateDTO;
import com.matheuss.controle_estoque_api.dto.ComputerResponseDTO;
import com.matheuss.controle_estoque_api.dto.ComputerUpdateDTO;
import com.matheuss.controle_estoque_api.mapper.ComputerMapper;
import com.matheuss.controle_estoque_api.repository.AssetRepository;
import com.matheuss.controle_estoque_api.repository.ComponentRepository;
import com.matheuss.controle_estoque_api.repository.ComputerRepository;
import com.matheuss.controle_estoque_api.repository.specification.ComputerSpecification; 
import com.matheuss.controle_estoque_api.service.support.EntityResolver;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ComputerService {

    private final ComputerRepository computerRepository;
    private final AssetRepository assetRepository;
    private final ComponentRepository componentRepository;
    private final ComputerMapper computerMapper;
    private final AssetHistoryService assetHistoryService;
    private final EntityResolver resolver;

    @Transactional
    public ComputerResponseDTO createComputer(ComputerCreateDTO dto) {
        // Lógica existente preservada
        if (assetRepository.existsByPatrimonio(dto.getPatrimonio())) {
            throw new IllegalStateException("Já existe um ativo com o número de patrimônio: " + dto.getPatrimonio());
        }
        if (dto.getAssetTag() != null && !dto.getAssetTag().isBlank() && assetRepository.existsByAssetTag(dto.getAssetTag())) {
            throw new IllegalStateException("Já existe um ativo com o Asset Tag: " + dto.getAssetTag());
        }
        
        Computer entity = computerMapper.toEntity(dto);
        entity.setCategory(resolver.requireCategory(dto.getCategoryId()));
        entity.setLocation(resolver.optionalLocation(dto.getLocationId()));
        entity.setStatus(AssetStatus.EM_ESTOQUE);
        entity.setCollaborator(null);
        Computer saved = computerRepository.save(entity);
        assetHistoryService.registerEvent(saved, HistoryEventType.CRIACAO, "Ativo cadastrado no sistema.", null);
        return computerMapper.toResponseDTO(saved);
    }

    // Método atualizado para usar Specifications para filtros dinâmicos.
 @Transactional(readOnly = true)
public Page<ComputerResponseDTO> getAllComputers(
        AssetStatus status, String name, String patrimonio, String serialNumber, Pageable pageable) {
    
    // Constrói a query dinâmica combinando todos os filtros com "and".
    Specification<Computer> spec = Specification.where(ComputerSpecification.hasStatus(status))
            .and(ComputerSpecification.nameContains(name))
            .and(ComputerSpecification.patrimonioContains(patrimonio))
            .and(ComputerSpecification.serialNumberContains(serialNumber));

    Page<Computer> computerPage = computerRepository.findAll(spec, pageable);
    
    return computerPage.map(computerMapper::toResponseDTO);
}

    @Transactional(readOnly = true)
    public ComputerResponseDTO getComputerById(Long id) {
        // Lógica existente preservada
        Computer entity = computerRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Computador não encontrado com o ID: " + id));
        return computerMapper.toResponseDTO(entity);
    }

    @Transactional
    public ComputerResponseDTO updateComputer(Long id, ComputerUpdateDTO dto) {
        // Lógica existente preservada
        Computer computer = resolver.requireComputer(id);
        Location newLocation = resolver.optionalLocation(dto.getLocationId());
        Collaborator newCollaborator = resolver.optionalCollaborator(dto.getCollaboratorId());

        if (dto.getPatrimonio() != null && !dto.getPatrimonio().isBlank() && !Objects.equals(computer.getPatrimonio(), dto.getPatrimonio())) {
            if (assetRepository.existsByPatrimonio(dto.getPatrimonio())) {
                throw new IllegalStateException("Operação não permitida: Já existe outro ativo com o patrimônio: " + dto.getPatrimonio());
            }
        }
        if (dto.getAssetTag() != null && !dto.getAssetTag().isBlank() && !Objects.equals(computer.getAssetTag(), dto.getAssetTag())) {
            if (assetRepository.existsByAssetTag(dto.getAssetTag())) {
                throw new IllegalStateException("Operação não permitida: Já existe outro ativo com o Asset Tag: " + dto.getAssetTag());
            }
        }

        computerMapper.updateEntityFromDto(dto, computer);
        if (dto.getCategoryId() != null) {
            computer.setCategory(resolver.requireCategory(dto.getCategoryId()));
        }

        Location oldLocation = computer.getLocation();
        Collaborator oldCollaborator = computer.getCollaborator();

        if (newLocation != null && newCollaborator != null) {
            throw new IllegalStateException("Operação não permitida: Um ativo não pode ser alocado para um colaborador e uma localização ao mesmo tempo.");
        }

        computer.setLocation(newLocation);
        computer.setCollaborator(newCollaborator);

        if (newLocation != null || newCollaborator != null) {
            computer.setStatus(AssetStatus.EM_USO);
        } else {
            computer.setStatus(AssetStatus.EM_ESTOQUE);
        }

        assetHistoryService.registerEvent(computer, HistoryEventType.ATUALIZACAO, "Dados do ativo foram atualizados.", null);

        if (!Objects.equals(oldLocation, newLocation)) {
            if (newLocation != null) {
               assetHistoryService.registerEvent(computer, HistoryEventType.ALOCACAO, "Ativo alocado para a localização PA: " + newLocation.getPaNumber(), null);
            }
            if (oldLocation != null) {
                assetHistoryService.registerEvent(computer, HistoryEventType.DEVOLUCAO, "Ativo devolvido da localização PA: " + oldLocation.getPaNumber(), null);
            }
        }
        if (!Objects.equals(oldCollaborator, newCollaborator)) {
            if (newCollaborator != null) {
                assetHistoryService.registerEvent(computer, HistoryEventType.ALOCACAO, "Ativo alocado para o colaborador: " + newCollaborator.getName(), null);
            }
            if (oldCollaborator != null) {
                assetHistoryService.registerEvent(computer, HistoryEventType.DEVOLUCAO, "Ativo devolvido pelo colaborador: " + oldCollaborator.getName(), null);
            }
        }

        Computer updatedComputer = computerRepository.save(computer);
        return computerMapper.toResponseDTO(updatedComputer);
    }

    @Transactional
    public ComputerResponseDTO swapComponent(Long computerId, Long componentToUninstallId, Long componentToInstallId) {
        // Lógica existente preservada
        Computer computer = resolver.requireComputer(computerId);
        Component componentToUninstall = resolver.requireComponent(componentToUninstallId);
        Component componentToInstall = resolver.requireComponent(componentToInstallId);

        if (!computer.getComponents().contains(componentToUninstall)) {
            throw new IllegalStateException(String.format("Operação não permitida: O componente '%s' (ID: %d) não está instalado no computador '%s'.",
                    componentToUninstall.getName(), componentToUninstallId, computer.getName()));
        }

        if (componentToInstall.getStatus() != AssetStatus.EM_ESTOQUE) {
            throw new IllegalStateException(String.format("Operação não permitida: O componente '%s' (ID: %d) não está em estoque e não pode ser instalado.",
                    componentToInstall.getName(), componentToInstallId));
        }

        if (!Objects.equals(componentToUninstall.getType(), componentToInstall.getType())) {
            throw new IllegalStateException(String.format("Operação não permitida: A troca só pode ser feita entre componentes do mesmo tipo. Tipo do componente atual: '%s', Tipo do novo componente: '%s'.",
                    componentToUninstall.getType(), componentToInstall.getType()));
        }

        componentToUninstall.setComputer(null);
        componentToUninstall.setStatus(AssetStatus.EM_ESTOQUE);

        componentToInstall.setComputer(computer);
        componentToInstall.setStatus(AssetStatus.EM_USO);

        String uninstallDetails = String.format("Componente '%s' (Tipo: %s) trocado e devolvido ao estoque.", componentToUninstall.getName(), componentToUninstall.getType());
        assetHistoryService.registerEvent(componentToUninstall, HistoryEventType.DEVOLUCAO, uninstallDetails, null);

        String installDetails = String.format("Componente '%s' (Tipo: %s) instalado via operação de troca no computador '%s'.", componentToInstall.getName(), componentToInstall.getType(), computer.getName());
        assetHistoryService.registerEvent(componentToInstall, HistoryEventType.INSTALACAO, installDetails, null);

        computerRepository.save(computer);
        componentRepository.save(componentToUninstall);
        componentRepository.save(componentToInstall);

        return getComputerById(computerId);
    }
}
