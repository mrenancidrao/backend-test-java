package com.backendtestjava.service;

import com.backendtestjava.model.Establishment;
import com.backendtestjava.model.Parking;
import com.backendtestjava.model.Vehicle;
import com.backendtestjava.repository.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public abstract class AbstractParkingService implements ParkingService {

    @Autowired
    protected ParkingRepository parkingRepository;

    @Override
    public Optional<Parking> findByVehicleAndExitDateTimeIsNull(Vehicle vehicle) {
        return parkingRepository.findByVehicleAndExitDateTimeIsNull(vehicle);
    }

    @Override
    public Optional<Parking> findByVehicleAndEstablishmentAndExitDateTimeIsNull(Vehicle vehicle, Establishment establishment) {
        return parkingRepository.findByVehicleAndEstablishmentAndExitDateTimeIsNull(vehicle, establishment);
    }

}
