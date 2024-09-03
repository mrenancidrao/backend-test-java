package com.backendtestjava.service;

import com.backendtestjava.model.Establishment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EstablishmentService {
    Establishment save(Establishment vehicle);

    Optional<Establishment> findById(UUID id);

    Optional<Establishment> findByCnpj(String cnpj);

    void delete(Establishment establishment);

    List<Establishment> findEstablishments(String name);
}
