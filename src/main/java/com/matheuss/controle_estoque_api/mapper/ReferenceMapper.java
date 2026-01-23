package com.matheuss.controle_estoque_api.mapper;

import com.matheuss.controle_estoque_api.domain.Category;
import com.matheuss.controle_estoque_api.domain.Computer;
import com.matheuss.controle_estoque_api.domain.Location;
import com.matheuss.controle_estoque_api.domain.User; // Import necessário
import com.matheuss.controle_estoque_api.dto.UserSimpleResponseDTO; // Import necessário
import com.matheuss.controle_estoque_api.repository.CategoryRepository;
import com.matheuss.controle_estoque_api.repository.ComputerRepository;
import com.matheuss.controle_estoque_api.repository.LocationRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReferenceMapper {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private ComputerRepository computerRepository;

    public Category toCategory(Long categoryId) {
        if (categoryId == null) return null;
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com o ID: " + categoryId));
    }

    public Location toLocation(Long locationId) {
        if (locationId == null) return null;
        return locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Localização não encontrada com o ID: " + locationId));
    }

    public Computer toComputer(Long computerId) {
        if (computerId == null || computerId == 0L) {
            return null;
        }
        return computerRepository.findById(computerId)
                .orElseThrow(() -> new EntityNotFoundException("Computador não encontrado com o ID: " + computerId));
    }

    /**
     * Converte uma entidade User completa para um DTO simplificado,
     * contendo apenas as informações essenciais para exibição.
     * Este método serve como a única fonte de verdade para esta conversão,
     * evitando ambiguidade nos mappers que o utilizam.
     *
     * @param user A entidade User a ser convertida.
     * @return Um UserSimpleResponseDTO preenchido ou null se o usuário for nulo.
     */
    public UserSimpleResponseDTO toUserSimpleDTO(User user) {
        if (user == null) {
            return null;
        }
        UserSimpleResponseDTO dto = new UserSimpleResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setUsername(user.getUsername());
        return dto;
    }
}
