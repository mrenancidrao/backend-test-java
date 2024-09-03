package com.backendtestjava.repository;

import com.backendtestjava.model.Establishment;
import com.backendtestjava.model.Parking;
import com.backendtestjava.model.Vehicle;
import com.backendtestjava.model.enums.VehicleTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParkingRepository extends JpaRepository<Parking, UUID> {

    Optional<Parking> findByVehicleAndExitDateTimeIsNull(Vehicle vehicle);
    Optional<Parking> findByVehicleAndEstablishmentAndExitDateTimeIsNull(Vehicle vehicle, Establishment establishment);
    List<Parking> findAllByEstablishment(Establishment establishment);
    List<Parking> findAllByEstablishmentAndExitDateTimeIsNull(Establishment establishment);
    @Query("SELECT COUNT(p) FROM Parking p WHERE p.establishment = :establishment AND p.exitDateTime IS NULL AND p.vehicle.type = :type")
    long countParkedVehiclesByEstablishmentAndType(Establishment establishment, VehicleTypeEnum type);
}
