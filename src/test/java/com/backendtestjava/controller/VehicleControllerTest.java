package com.backendtestjava.controller;

import com.backendtestjava.model.Vehicle;
import com.backendtestjava.model.enums.ColorEnum;
import com.backendtestjava.model.enums.VehicleBrandEnum;
import com.backendtestjava.model.enums.VehicleTypeEnum;
import com.backendtestjava.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VehicleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleService vehicleService;

    private Vehicle createExistingVehicle(UUID vehicleId) {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);
        vehicle.setBrand(VehicleBrandEnum.FIAT);
        vehicle.setModel("Cronos");
        vehicle.setColor(ColorEnum.BRANCO);
        vehicle.setLicencePlate("OCR5YBC");
        vehicle.setType(VehicleTypeEnum.CAR);
        return vehicle;
    }

    private String createVehicleJson(String model, String color, String licencePlate, String type) {
        return String.format("""
                {
                    "brand": "FIAT",
                    "model": "%s",
                    "color": "%s",
                    "licencePlate": "%s",
                    "type": "%s"
                }
                """, model, color, licencePlate, type);
    }

    @Test
    public void shouldCreateVehicleWithSuccess() throws Exception {
        var vehicleJson = createVehicleJson("CRONOS", "BRANCO", "OCR5YBC", "CAR");

        mockMvc.perform(post("/vehicles")
                        .contentType("application/json")
                        .content(vehicleJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnBadRequestWhenVehicleDataIsInvalid() throws Exception {
        var incompleteVehicleJson = """
                {
                    "brand": "FIAT",
                    "color": "BRANCO"
                }
                """;

        mockMvc.perform(post("/vehicles")
                        .contentType("application/json")
                        .content(incompleteVehicleJson))
                .andExpect(status().isBadRequest());

        var invalidVehicleJson = createVehicleJson("CRONOS", "INVALID_COLOR", "", "UNKNOWN_TYPE");

        mockMvc.perform(post("/vehicles")
                        .contentType("application/json")
                        .content(invalidVehicleJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldUpdateVehicleWithSuccess() throws Exception {
        UUID vehicleId = UUID.randomUUID();
        Vehicle existingVehicle = createExistingVehicle(vehicleId);
        String updatedVehicleJson = createVehicleJson("ARGO", "PRETO", "XYZ1234", "CAR");

        when(vehicleService.findById(vehicleId)).thenReturn(Optional.of(existingVehicle));

        mockMvc.perform(put("/vehicles/" + vehicleId)
                        .contentType("application/json")
                        .content(updatedVehicleJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/vehicles/" + vehicleId))
                .andExpect(status().isOk())
                .andExpect(content().json(updatedVehicleJson));
    }

    @Test
    public void shouldReturnNotFoundWhenUpdatingNonExistentVehicle() throws Exception {
        UUID vehicleId = UUID.randomUUID();
        var vehicleJson = createVehicleJson("CRONOS", "BRANCO", "OCR5YBC", "CAR");

        mockMvc.perform(put("/vehicles/" + vehicleId)
                        .contentType("application/json")
                        .content(vehicleJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteVehicleWithSuccess() throws Exception {
        UUID vehicleId = UUID.randomUUID();
        Vehicle existingVehicle = createExistingVehicle(vehicleId);

        when(vehicleService.findById(vehicleId)).thenReturn(Optional.of(existingVehicle));

        mockMvc.perform(delete("/vehicles/" + vehicleId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnNotFoundWhenDeletingNonExistentVehicle() throws Exception {
        UUID vehicleId = UUID.randomUUID();

        mockMvc.perform(delete("/vehicles/" + vehicleId))
                .andExpect(status().isNotFound());
    }
}
