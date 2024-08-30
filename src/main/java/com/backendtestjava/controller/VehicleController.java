package com.backendtestjava.controller;

import com.backendtestjava.model.Vehicle;
import com.backendtestjava.model.dtos.VehicleDto;
import com.backendtestjava.model.enums.ColorEnum;
import com.backendtestjava.model.enums.VehicleBrandEnum;
import com.backendtestjava.model.enums.VehicleTypeEnum;
import com.backendtestjava.service.VehicleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/vehicles")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
public class VehicleController {

    VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<Object> saveVehicle(@RequestBody @Valid VehicleDto vehicleDto) {
        var vehicle = new Vehicle();

        BeanUtils.copyProperties(vehicleDto, vehicle);
        vehicle.setLicencePlate(vehicleDto.licencePlate().toUpperCase());
        vehicle.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        vehicle.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.save(vehicle));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteVehicle(@PathVariable(value = "id") UUID id) {
        Optional<Vehicle> vehicleOptional = vehicleService.findById(id);
        if(!vehicleOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veículo não encontrado");
        }

        vehicleService.delete(vehicleOptional.get());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateVehicle(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid VehicleDto vehicleDto) {
        Optional<Vehicle> vehicleOptional = vehicleService.findById(id);
        if(!vehicleOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veículo não encontrado");
        }

        var vehicleModel = vehicleOptional.get();
        BeanUtils.copyProperties(vehicleDto, vehicleModel);
        vehicleModel.setLicencePlate(vehicleDto.licencePlate().toUpperCase());
        vehicleModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.OK).body(vehicleService.save(vehicleModel));
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> getVehicles(
            @RequestParam(value = "brand", required = false) VehicleBrandEnum brand,
            @RequestParam(value = "model", required = false) String model,
            @RequestParam(value = "color", required = false) ColorEnum color,
            @RequestParam(value = "type", required = false) VehicleTypeEnum type) {

        List<Vehicle> vehicles = vehicleService.findVehicles(brand, model, color, type);

        if (vehicles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
        }

        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/{licensePlate}")
    public ResponseEntity<Object> getVehicleByLicensePlate(@PathVariable(value = "licensePlate") String licensePlate) {
        Optional<Vehicle> vehicleModelOptional = vehicleService.findByLicencePlate(licensePlate.toUpperCase());
        return vehicleModelOptional.<ResponseEntity<Object>>map(vehicle -> ResponseEntity.status(HttpStatus.OK).body(vehicle)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veículo não encontrado."));
    }
}
