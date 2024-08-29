package com.backendtestjava.service;

import com.backendtestjava.model.Vehicle;
import com.backendtestjava.model.enums.ColorEnum;
import com.backendtestjava.model.enums.VehicleBrandEnum;
import com.backendtestjava.model.enums.VehicleTypeEnum;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehicleService {
    Vehicle save(Vehicle vehicle);

    List<Vehicle> findAll();

    Optional<Vehicle> findById(UUID id);

    void delete(Vehicle vehicle);

    List<Vehicle> findVehicles(VehicleBrandEnum brand, String model, ColorEnum color, VehicleTypeEnum type);

    Optional<Vehicle> findByLicencePlate(String licencePlate);
}
