package com.backendtestjava.repository;

import com.backendtestjava.model.Establishment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EstablishmentRepository extends JpaRepository<Establishment, UUID> {
    Optional<Establishment> findByCnpj(String cnpj);

    @Query("SELECT e FROM Establishment e WHERE "
            + "(:name IS NULL OR e.name ILIKE %:name%)")
    List<Establishment> findByFilters(@Param("name") String name);
}
