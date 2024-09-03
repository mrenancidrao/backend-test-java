package com.backendtestjava.model.dtos;

import com.backendtestjava.model.enums.ColorEnum;
import com.backendtestjava.model.enums.VehicleBrandEnum;
import com.backendtestjava.model.enums.VehicleTypeEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

public record VehicleDTO(@NotNull VehicleBrandEnum brand,
                         @NotNull String model,
                         @NotNull ColorEnum color,
                         @NotNull @Pattern(regexp = "^[A-Za-z]{3}[A-Za-z0-9]{4}$") String licencePlate,
                         @NotNull VehicleTypeEnum type) implements Serializable {
}
