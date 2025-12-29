package com.matheuss.controle_estoque_api.repository;

import com.matheuss.controle_estoque_api.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    // O JpaRepository já nos fornece métodos como findAll(), findById(), save(), deleteById(), etc.
    // Por enquanto, não precisamos de métodos customizados aqui.
}
