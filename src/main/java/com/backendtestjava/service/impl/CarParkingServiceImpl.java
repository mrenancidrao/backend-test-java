package com.backendtestjava.service.impl;

import com.backendtestjava.model.Establishment;
import com.backendtestjava.model.Parking;
import com.backendtestjava.model.Vehicle;
import com.backendtestjava.model.enums.VehicleTypeEnum;
import com.backendtestjava.service.AbstractParkingService;
import com.backendtestjava.service.EstablishmentService;
import com.backendtestjava.service.VehicleService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service("car")
@Primary
@AllArgsConstructor
public class CarParkingServiceImpl extends AbstractParkingService {

    private final VehicleService vehicleService;
    private final EstablishmentService establishmentService;

    @Override
    public Parking parkVehicle(Vehicle vehicle, UUID establishmentId) {

        if (vehicle.isParked()) {
            throw new IllegalStateException("O carro já está estacionado");
        }

        var establishment = establishmentService.findById(establishmentId)
                .orElseThrow(() -> new IllegalArgumentException("Estacionamento não encontrado"));

        if (availableLimit(establishment) < 1L) {
            throw new IllegalStateException("Não há vagas disponíveis para carro");
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
                .orElseThrow(() -> new IllegalArgumentException("Carro não está no estacionamento"));

        parking.setExitDateTime(LocalDateTime.now(ZoneId.of("UTC")));
        parking.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return parkingRepository.save(parking);
    }

    @Override
    public Long availableLimit(Establishment establishment) {
        var parkingList = parkingRepository.findAllByEstablishmentAndExitDateTimeIsNull(establishment);

        Long parkeds = parkingList.stream()
                .filter(parking -> parking.getVehicle().getType().equals(VehicleTypeEnum.CAR))
                .count();

        return establishment.getNumberCarSpaces() - parkeds;
    }
}
