package com.backendtestjava.model.enums;

public enum VehicleTypeEnum {
    CAR("Carro"), MOTORCYCLE("Moto");

    private final String displayName;

    VehicleTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}