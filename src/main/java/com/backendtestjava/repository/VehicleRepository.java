package com.backendtestjava.repository;

import com.backendtestjava.model.Vehicle;
import com.backendtestjava.model.enums.ColorEnum;
import com.backendtestjava.model.enums.VehicleBrandEnum;
import com.backendtestjava.model.enums.VehicleTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    @Query("SELECT v FROM Vehicle v WHERE "
            + "(:brand IS NULL OR v.brand = :brand) AND "
            + "(:model IS NULL OR v.model LIKE %:model%) AND "
            + "(:color IS NULL OR v.color = :color) AND "
            + "(:type IS NULL OR v.type = :type)")
    List<Vehicle> findByFilters(@Param("brand") VehicleBrandEnum brand,
                                @Param("model") String model,
                                @Param("color") ColorEnum color,
                                @Param("type") VehicleTypeEnum type);

    Optional<Vehicle> findByLicencePlate(String licencePlate);
}
