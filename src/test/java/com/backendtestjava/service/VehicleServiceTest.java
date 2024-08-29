package com.backendtestjava.service;

import com.backendtestjava.model.Vehicle;
import com.backendtestjava.model.enums.ColorEnum;
import com.backendtestjava.model.enums.VehicleBrandEnum;
import com.backendtestjava.model.enums.VehicleTypeEnum;
import com.backendtestjava.repository.VehicleRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    public void shouldCreateVehicleWithSuccess() {

        var vehicle = new Vehicle();
        vehicle.setBrand(VehicleBrandEnum.FIAT);
        vehicle.setModel("CRONOS");
        vehicle.setColor(ColorEnum.BRANCO);
        vehicle.setLicencePlate("OCR5YBC");
        vehicle.setType(VehicleTypeEnum.CAR);
        vehicle.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        vehicle.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        var savedVehicle = vehicleService.save(vehicle);

        verify(vehicleRepository).save(any(Vehicle.class));

        ArgumentCaptor<Vehicle> vehicleCaptor = forClass(Vehicle.class);
        verify(vehicleRepository).save(vehicleCaptor.capture());

        Vehicle capturedVehicle = vehicleCaptor.getValue();
        assertEquals("FIAT", capturedVehicle.getBrand());
        assertEquals("CRONOS", capturedVehicle.getModel());
        assertEquals("BRANCO", capturedVehicle.getColor());
        assertEquals("OCR5YBC", capturedVehicle.getLicencePlate());
        assertEquals("CAR", capturedVehicle.getType());

        assertNotNull(savedVehicle);
    }

    @Test
    public void shouldCreateVehicleWithValidationError() {

        var vehicle = new Vehicle();
        vehicle.setModel(null);
        vehicle.setColor(ColorEnum.BRANCO);
        vehicle.setLicencePlate(null);
        vehicle.setType(VehicleTypeEnum.CAR);
        vehicle.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        vehicle.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        assertThrows(ConstraintViolationException.class, () -> {
            vehicleService.save(vehicle);
        });

        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }
}
