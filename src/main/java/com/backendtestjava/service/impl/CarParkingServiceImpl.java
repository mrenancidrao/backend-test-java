package com.backendtestjava.service.impl;

import com.backendtestjava.model.Parking;
import com.backendtestjava.model.dtos.ParkingDto;
import com.backendtestjava.service.AbstractParkingService;
import com.backendtestjava.service.EstablishmentService;
import com.backendtestjava.service.VehicleService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service("car")
@Primary
@AllArgsConstructor
public class CarParkingServiceImpl extends AbstractParkingService {

    private final VehicleService vehicleService;
    private final EstablishmentService establishmentService;

    @Override
    public Parking parkVehicle(ParkingDto parkingDto) {
        var vehicle = vehicleService.findByLicencePlate(parkingDto.licensePlate())
                .orElseThrow(() -> new IllegalArgumentException("Carro não encontrado"));

        var establishment = establishmentService.findById(parkingDto.establishmentId())
                .orElseThrow(() -> new IllegalArgumentException("Estacionamento não encontrado"));

        if (vehicle.isParked()) {
            throw new IllegalStateException("O carro já está estacionado");
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
    public Parking unparkVehicle(ParkingDto parkingDto) {
        var vehicle = vehicleService.findByLicencePlate(parkingDto.licensePlate())
                .orElseThrow(() -> new IllegalArgumentException("Carro não encontrado"));

        var establishment = establishmentService.findById(parkingDto.establishmentId())
                .orElseThrow(() -> new IllegalArgumentException("Estacionamento não encontrado"));

        var parking = parkingRepository.findByVehicleAndEstablishmentAndExitDateTimeIsNull(vehicle, establishment)
                .orElseThrow(() -> new IllegalArgumentException("Carro não está no estacionamento"));

        parking.setExitDateTime(LocalDateTime.now(ZoneId.of("UTC")));
        parking.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return parkingRepository.save(parking);
    }
}
