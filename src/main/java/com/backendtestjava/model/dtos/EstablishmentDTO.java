package com.backendtestjava.model.dtos;

import com.backendtestjava.model.Address;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

public record EstablishmentDTO(@NotNull String name,
                               @NotNull @Pattern(regexp = "(^\\d{2})\\d{12}$") String cnpj,
                               @NotNull Address address,
                               @NotNull String phone,
                               @NotNull @Min(0) @Max(9999) long numberCarSpaces,
                               @NotNull @Min(0) @Max(9999) long numberMotorcycleSpaces) implements Serializable {
}
