package com.backendtestjava.service.impl;

import com.backendtestjava.model.Establishment;
import com.backendtestjava.repository.EstablishmentRepository;
import com.backendtestjava.service.EstablishmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EstablishmentServiceImpl implements EstablishmentService {

    EstablishmentRepository establishmentRepository;

    @Override
    public Establishment save(Establishment establishment) {
        return establishmentRepository.save(establishment);
    }

    @Override
    public Optional<Establishment> findById(UUID id) {
        return establishmentRepository.findById(id);
    }

    @Override
    public Optional<Establishment> findByCnpj(String cnpj) {
        return establishmentRepository.findByCnpj(cnpj);
    }

    @Override
    public void delete(Establishment establishment) {
        establishmentRepository.delete(establishment);
    }

    @Override
    public List<Establishment> findEstablishments(String name) {
        return establishmentRepository.findByFilters(name);
    }
}
