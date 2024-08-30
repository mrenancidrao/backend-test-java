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

import java.time.LocalDateTime;
import java.time.ZoneId;

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
        var vehicle = vehicleService.findByLicencePlate(parkingDto.licensePlate());
        var establishment = establishmentService.findById(parkingDto.establishmentId());

        if(!vehicle.isPresent() || !establishment.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veículo ou estacionamento não encontrado");
        }

        if(vehicle.get().isParked()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("O veículo já está estacionado");
        }

        var parking = new Parking();
        parking.setVehicle(vehicle.get());
        parking.setEstablishment(establishment.get());
        parking.setEntryDateTime(LocalDateTime.now(ZoneId.of("UTC")));
        parking.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        parking.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(ParkingService.qualifier(parking.getVehicle().getType()).parkVehicle(parking));
    }

    @PostMapping("/getOut")
    public ResponseEntity<Object> unparkVehicle(@RequestBody @Valid ParkingDto parkingDto) {
        var vehicle = vehicleService.findByLicencePlate(parkingDto.licensePlate());
        var establishment = establishmentService.findById(parkingDto.establishmentId());

        if(!vehicle.isPresent() || !establishment.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veículo ou estacionamento não encontrado");
        }

        var parking = parkingService.findByVehicleAndEstablishmentAndExitDateTimeIsNull(vehicle.get(), establishment.get());
        if (!parking.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veículo não está no estacionamento");
        }

        var parkingUpdated = parking.get();
        parkingUpdated.setExitDateTime(LocalDateTime.now(ZoneId.of("UTC")));
        parkingUpdated.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(ParkingService.qualifier(parkingUpdated.getVehicle().getType()).unparkVehicle(parkingUpdated));
    }
}
