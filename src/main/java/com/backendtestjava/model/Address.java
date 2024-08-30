package com.backendtestjava.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Embeddable
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {

    @NotNull
    private String street;

    @NotNull
    private String number;

    private String complement;

    private String postalCode;

    private String neighborhood;

    @NotNull
    private String city;

    private String state;
}
