package com.backendtestjava.model.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ParkingDto(@NotNull String licensePlate, @NotNull UUID establishmentId) {
}
