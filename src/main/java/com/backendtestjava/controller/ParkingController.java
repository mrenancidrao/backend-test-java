package com.backendtestjava.controller;

import com.backendtestjava.model.Parking;
import com.backendtestjava.model.dtos.ParkingDto;
import com.backendtestjava.service.EstablishmentService;
import com.backendtestjava.service.ParkingService;
import com.backendtestjava.service.VehicleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parking")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
public class ParkingController {

    ParkingService parkingService;

    VehicleService vehicleService;

    EstablishmentService establishmentService;

    @PostMapping("/getIn")
    public ResponseEntity<Object> parkVehicle(@RequestBody @Valid ParkingDto parkingDto) {
        try {
            var vehicle = vehicleService.findByLicencePlate(parkingDto.licensePlate());
            Parking parkedVehicle = ParkingService.qualifier(vehicle.get().getType()).parkVehicle(parkingDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(parkedVehicle);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @PostMapping("/getOut")
    public ResponseEntity<Object> unparkVehicle(@RequestBody @Valid ParkingDto parkingDto) {
        try {
            var vehicle = vehicleService.findByLicencePlate(parkingDto.licensePlate());
            Parking unparkedVehicle = ParkingService.qualifier(vehicle.get().getType()).unparkVehicle(parkingDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(unparkedVehicle);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
