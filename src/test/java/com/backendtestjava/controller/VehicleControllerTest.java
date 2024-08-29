package com.backendtestjava.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VehicleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnAllVehicles() throws Exception {
        mockMvc.perform(get("/vehicles"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldCreateVehicleWithSuccess() throws Exception {
        var vehicleJson = """
                {
                    "brand": "FIAT",
                    "model": "CRONOS",
                    "color": "BRANCO",
                    "licencePlate": "OCR5YBC",
                    "type": "CAR"
                }
                """;

        mockMvc.perform(post("/vehicles")
                        .contentType("application/json")
                        .content(vehicleJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnBadRequestWhenVehicleDataIsInvalid() throws Exception {
        var vehicleJson = """
                {
                    "brand": "FIAT",
                    "color": "BRANCO"
                }
                """;

        mockMvc.perform(post("/vehicles")
                        .contentType("application/json")
                        .content(vehicleJson))
                .andExpect(status().isBadRequest());

        var invalidFieldValuesJson = """
                {
                    "brand": "FIAT",
                    "model": "CRONOS",
                    "color": "INVALID_COLOR",
                    "licencePlate": "",
                    "type": "UNKNOWN_TYPE"
                }
                """;

        mockMvc.perform(post("/vehicles")
                        .contentType("application/json")
                        .content(invalidFieldValuesJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldUpdateVehicleWithSuccess() throws Exception {
        UUID vehicleId = UUID.randomUUID();

        var updatedVehicleJson = """
                {
                    "brand": "FIAT",
                    "model": "ARGO",
                    "color": "PRETO",
                    "licencePlate": "XYZ1234",
                    "type": "CAR"
                }
                """;

        mockMvc.perform(put("/vehicles/" + vehicleId)
                        .contentType("application/json")
                        .content(updatedVehicleJson))
                .andExpect(status().isOk());

        // Optionally, you can check if the vehicle was updated correctly
        mockMvc.perform(get("/vehicles/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(updatedVehicleJson));
    }

    @Test
    public void shouldReturnNotFoundWhenUpdatingNonExistentVehicle() throws Exception {
        var vehicleJson = """
                {
                    "brand": "FIAT",
                    "model": "CRONOS",
                    "color": "BRANCO",
                    "licencePlate": "OCR5YBC",
                    "type": "CAR"
                }
                """;

        mockMvc.perform(put("/vehicles/999")
                        .contentType("application/json")
                        .content(vehicleJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteVehicleWithSuccess() throws Exception {
        UUID vehicleId = UUID.randomUUID();

        mockMvc.perform(delete("/vehicles/" + vehicleId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnNotFoundWhenDeletingNonExistentVehicle() throws Exception {
        mockMvc.perform(delete("/vehicles/999"))
                .andExpect(status().isNotFound());
    }
}