package com.backendtestjava.service;

import com.backendtestjava.model.Vehicle;
import com.backendtestjava.model.enums.ColorEnum;
import com.backendtestjava.model.enums.VehicleBrandEnum;
import com.backendtestjava.model.enums.VehicleTypeEnum;
import com.backendtestjava.repository.VehicleRepository;
import com.backendtestjava.service.impl.VehicleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private Vehicle vehicle;

    @BeforeEach
    public void setUp() {
        vehicle = new Vehicle();
        vehicle.setBrand(VehicleBrandEnum.FIAT);
        vehicle.setModel("CRONOS");
        vehicle.setColor(ColorEnum.BRANCO);
        vehicle.setLicencePlate("OCR5YBC");
        vehicle.setType(VehicleTypeEnum.CAR);
        vehicle.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        vehicle.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
    }

    @Test
    void shouldCreateVehicleWithSuccess() {
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        Vehicle savedVehicle = vehicleService.save(vehicle);

        ArgumentCaptor<Vehicle> vehicleCaptor = forClass(Vehicle.class);
        verify(vehicleRepository).save(vehicleCaptor.capture());

        Vehicle capturedVehicle = vehicleCaptor.getValue();

        assertEquals(VehicleBrandEnum.FIAT, capturedVehicle.getBrand());
        assertEquals("CRONOS", capturedVehicle.getModel());
        assertEquals(ColorEnum.BRANCO, capturedVehicle.getColor());
        assertEquals("OCR5YBC", capturedVehicle.getLicencePlate());
        assertEquals(VehicleTypeEnum.CAR, capturedVehicle.getType());

        assertNotNull(savedVehicle);
    }

    @Test
    void shouldFindVehicleByIdWithSuccess() {
        UUID vehicleId = UUID.randomUUID();
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        Optional<Vehicle> foundVehicle = vehicleService.findById(vehicleId);

        assertTrue(foundVehicle.isPresent());
        assertEquals(vehicle, foundVehicle.get());
    }

    @Test
    void shouldReturnEmptyWhenVehicleNotFound() {
        UUID vehicleId = UUID.randomUUID();
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        Optional<Vehicle> foundVehicle = vehicleService.findById(vehicleId);

        assertFalse(foundVehicle.isPresent());
    }

    @Test
    void shouldDeleteVehicleWithSuccess() {
        vehicleService.delete(vehicle);

        verify(vehicleRepository, times(1)).delete(vehicle);
    }

    @Test
    void shouldFindVehicleByLicencePlateWithSuccess() {
        String licencePlate = "OCR5YBC";
        when(vehicleRepository.findByLicencePlate(licencePlate)).thenReturn(Optional.of(vehicle));

        Optional<Vehicle> foundVehicle = vehicleService.findByLicencePlate(licencePlate);

        assertTrue(foundVehicle.isPresent());
        assertEquals(vehicle, foundVehicle.get());
    }

    @Test
    void shouldReturnEmptyWhenVehicleNotFoundByLicencePlate() {
        String licencePlate = "INVALID123";
        when(vehicleRepository.findByLicencePlate(licencePlate)).thenReturn(Optional.empty());

        Optional<Vehicle> foundVehicle = vehicleService.findByLicencePlate(licencePlate);

        assertFalse(foundVehicle.isPresent());
    }
}
