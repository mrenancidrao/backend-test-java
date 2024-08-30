package com.backendtestjava.service;

import com.backendtestjava.config.SpringContext;
import com.backendtestjava.model.Establishment;
import com.backendtestjava.model.Parking;
import com.backendtestjava.model.Vehicle;
import com.backendtestjava.model.enums.VehicleTypeEnum;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParkingService {
    Parking parkVehicle(Vehicle vehicle, UUID establishmentId);

    Parking unparkVehicle(Vehicle vehicle, UUID establishmentId);

    List<Parking> findAllByEstablishment(Establishment establishment);

    Optional<Parking> findByVehicleAndExitDateTimeIsNull(Vehicle vehicle);

    Optional<Parking> findByVehicleAndEstablishmentAndExitDateTimeIsNull(Vehicle vehicle, Establishment establishment);

    Long availableLimit(Establishment establishment);

    /**
     * Qualifica o Serviço do Parking através do typeVehicle usando o pattern strategy.
     * @param typeVehicle string typeVehicle.
     * @return Chain by Qualifier name.
     */
    static ParkingService qualifier(final VehicleTypeEnum typeVehicle) {
        return (ParkingService) SpringContext.getBean(typeVehicle.toString().toLowerCase());
    }
}
