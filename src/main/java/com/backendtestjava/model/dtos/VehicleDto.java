package com.backendtestjava.model.dtos;

import com.backendtestjava.model.enums.ColorEnum;
import com.backendtestjava.model.enums.VehicleBrandEnum;
import com.backendtestjava.model.enums.VehicleTypeEnum;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record VehicleDto(@NotNull VehicleBrandEnum brand,
                         @NotNull String model,
                         @NotNull ColorEnum color,
                         @NotNull String licencePlate,
                         @NotNull VehicleTypeEnum type) implements Serializable {
}
