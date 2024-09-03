package com.backendtestjava.service;

import com.backendtestjava.model.Establishment;
import com.backendtestjava.model.Parking;
import com.backendtestjava.model.Vehicle;
import com.backendtestjava.model.enums.VehicleTypeEnum;
import com.backendtestjava.repository.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public abstract class AbstractParkingService implements ParkingService {

    @Autowired
    protected ParkingRepository parkingRepository;

    @Override
    public List<Parking> findAllByEstablishment(Establishment establishment) {
        return parkingRepository.findAllByEstablishment(establishment);
    }

    @Override
    public Optional<Parking> findByVehicleAndExitDateTimeIsNull(Vehicle vehicle) {
        return parkingRepository.findByVehicleAndExitDateTimeIsNull(vehicle);
    }

    @Override
    public Optional<Parking> findByVehicleAndEstablishmentAndExitDateTimeIsNull(Vehicle vehicle, Establishment establishment) {
        return parkingRepository.findByVehicleAndEstablishmentAndExitDateTimeIsNull(vehicle, establishment);
    }

    @Override
    public Long availableLimit(Establishment establishment, VehicleTypeEnum type) {

        long availableSpaces;
        var parkeds = parkingRepository.countParkedVehiclesByEstablishmentAndType(establishment, type);
        if (type == VehicleTypeEnum.CAR) {
            availableSpaces = establishment.getNumberCarSpaces();
        } else if (type == VehicleTypeEnum.MOTORCYCLE) {
            availableSpaces = establishment.getNumberMotorcycleSpaces();
        } else {
            throw new IllegalArgumentException("Tipo de veículo não encontrado");
        }

        return availableSpaces - parkeds;
    }
}
