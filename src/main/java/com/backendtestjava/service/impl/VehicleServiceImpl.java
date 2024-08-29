package com.backendtestjava.service.impl;

import com.backendtestjava.model.Vehicle;
import com.backendtestjava.model.enums.ColorEnum;
import com.backendtestjava.model.enums.VehicleBrandEnum;
import com.backendtestjava.model.enums.VehicleTypeEnum;
import com.backendtestjava.repository.VehicleRepository;
import com.backendtestjava.service.VehicleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    VehicleRepository vehicleRepository;

    @Override
    public Vehicle save(@Valid Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Override
    public Optional<Vehicle> findById(UUID id) {
        return vehicleRepository.findById(id);
    }

    @Override
    public void delete(Vehicle vehicle) {
        vehicleRepository.delete(vehicle);
    }

    @Override
    public List<Vehicle> findVehicles(VehicleBrandEnum brand, String model, ColorEnum color, VehicleTypeEnum type) {
        return vehicleRepository.findByFilters(brand, model, color, type);
    }

    @Override
    public Optional<Vehicle> findByLicencePlate(String licencePlate) {
        return vehicleRepository.findByLicencePlate(licencePlate);
    }
}
