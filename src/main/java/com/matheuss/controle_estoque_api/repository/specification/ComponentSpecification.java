package com.matheuss.controle_estoque_api.repository.specification;

import com.matheuss.controle_estoque_api.domain.Component;
import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import org.springframework.data.jpa.domain.Specification;

public class ComponentSpecification {

    // Filtro por status (busca exata).
    public static Specification<Component> hasStatus(AssetStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    // Filtro por tipo (busca parcial, "contém").
    public static Specification<Component> typeContains(String type) {
        return (root, query, criteriaBuilder) -> {
            if (type == null || type.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("type")), "%" + type.toLowerCase() + "%");
        };
    }

    // Filtro por nome (busca parcial, "contém").
    public static Specification<Component> nameContains(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }
}
