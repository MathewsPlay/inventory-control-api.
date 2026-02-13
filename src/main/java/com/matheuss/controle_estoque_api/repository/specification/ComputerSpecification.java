package com.matheuss.controle_estoque_api.repository.specification;

import com.matheuss.controle_estoque_api.domain.Computer;
import com.matheuss.controle_estoque_api.domain.enums.AssetStatus;
import org.springframework.data.jpa.domain.Specification;

public class ComputerSpecification {

    // Filtro por status (busca exata).
    public static Specification<Computer> hasStatus(AssetStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    // Filtro por nome (busca parcial, "contém", ignorando maiúsculas/minúsculas).
    public static Specification<Computer> nameContains(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    // Filtro por patrimônio (busca parcial, "contém", ignorando maiúsculas/minúsculas).
    public static Specification<Computer> patrimonioContains(String patrimonio) {
        return (root, query, criteriaBuilder) -> {
            if (patrimonio == null || patrimonio.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("patrimonio")), "%" + patrimonio.toLowerCase() + "%");
        };
    }

    // Filtro por número de série (busca parcial, "contém", ignorando maiúsculas/minúsculas).
    public static Specification<Computer> serialNumberContains(String serialNumber) {
        return (root, query, criteriaBuilder) -> {
            if (serialNumber == null || serialNumber.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("serialNumber")), "%" + serialNumber.toLowerCase() + "%");
        };
    }
}
