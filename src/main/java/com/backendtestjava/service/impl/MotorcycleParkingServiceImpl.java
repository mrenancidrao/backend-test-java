package com.backendtestjava.service.impl;

import com.backendtestjava.model.Parking;
import com.backendtestjava.model.Vehicle;
import com.backendtestjava.service.AbstractParkingService;
import com.backendtestjava.service.EstablishmentService;
import com.backendtestjava.service.VehicleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service("motorcycle")
@AllArgsConstructor
public class MotorcycleParkingServiceImpl extends AbstractParkingService {
    private final VehicleService vehicleService;
    private final EstablishmentService establishmentService;

    @Override
    public Parking parkVehicle(Vehicle vehicle, UUID establishmentId) {

        var establishment = establishmentService.findById(establishmentId)
                .orElseThrow(() -> new IllegalArgumentException("Estacionamento não encontrado"));

        if (vehicle.isParked()) {
            throw new IllegalStateException("A moto já está estacionada");
        }

        Parking parking = new Parking();
        parking.setVehicle(vehicle);
        parking.setEstablishment(establishment);
        parking.setEntryDateTime(LocalDateTime.now(ZoneId.of("UTC")));
        parking.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        parking.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return parkingRepository.save(parking);
    }

    @Override
    public Parking unparkVehicle(Vehicle vehicle, UUID establishmentId) {

        var establishment = establishmentService.findById(establishmentId)
                .orElseThrow(() -> new IllegalArgumentException("Estacionamento não encontrado"));

        var parking = parkingRepository.findByVehicleAndEstablishmentAndExitDateTimeIsNull(vehicle, establishment)
                .orElseThrow(() -> new IllegalArgumentException("Moto não está no estacionamento"));

        parking.setExitDateTime(LocalDateTime.now(ZoneId.of("UTC")));
        parking.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return parkingRepository.save(parking);
    }
}
