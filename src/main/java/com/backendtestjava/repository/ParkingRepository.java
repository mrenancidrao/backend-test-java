package com.backendtestjava.repository;

import com.backendtestjava.model.Establishment;
import com.backendtestjava.model.Parking;
import com.backendtestjava.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParkingRepository extends JpaRepository<Parking, UUID> {
    Optional<Parking> findByVehicleAndExitDateTimeIsNull(Vehicle vehicle);
    Optional<Parking> findByVehicleAndEstablishmentAndExitDateTimeIsNull(Vehicle vehicle, Establishment establishment);
    List<Parking> findAllByEstablishment(Establishment establishment);
}
