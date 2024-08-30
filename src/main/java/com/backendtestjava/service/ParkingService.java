package com.backendtestjava.service;

import com.backendtestjava.config.SpringContext;
import com.backendtestjava.model.Establishment;
import com.backendtestjava.model.Parking;
import com.backendtestjava.model.Vehicle;
import com.backendtestjava.model.dtos.ParkingDto;
import com.backendtestjava.model.enums.VehicleTypeEnum;

import java.util.Optional;

public interface ParkingService {
    Parking parkVehicle(ParkingDto parkingDto);

    Parking unparkVehicle(ParkingDto parkingDto);

    Optional<Parking> findByVehicleAndExitDateTimeIsNull(Vehicle vehicle);

    Optional<Parking> findByVehicleAndEstablishmentAndExitDateTimeIsNull(Vehicle vehicle, Establishment establishment);

    /**
     * Qualifica o Serviço do Parking através do typeVehicle usando o pattern strategy.
     * @param typeVehicle string typeVehicle.
     * @return Chain by Qualifier name.
     */
    static ParkingService qualifier(final VehicleTypeEnum typeVehicle) {
        return (ParkingService) SpringContext.getBean(typeVehicle.toString().toLowerCase());
    }
}
