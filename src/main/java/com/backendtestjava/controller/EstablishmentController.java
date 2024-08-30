package com.backendtestjava.controller;

import com.backendtestjava.model.Establishment;
import com.backendtestjava.model.dtos.EstablishmentDto;
import com.backendtestjava.service.EstablishmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
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
@RequestMapping("/establishments")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
public class EstablishmentController {

    EstablishmentService establishmentService;

    @PostMapping
    public ResponseEntity<Object> saveEstablishment(@RequestBody @Valid EstablishmentDto establishmentDto) {
        var establishment = new Establishment();

        BeanUtils.copyProperties(establishmentDto, establishment);
        establishment.setName(establishmentDto.name().toUpperCase());
        establishment.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        establishment.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED).body(establishmentService.save(establishment));
    }

    @GetMapping
    public ResponseEntity<List<Establishment>> getEstablishments(
            @RequestParam(value = "name", required = false) String name) {

        List<Establishment> establishments = establishmentService.findEstablishments(name);

        if (establishments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
        }

        return ResponseEntity.ok(establishments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEstablishment(@PathVariable(value = "id") UUID id) {
        Optional<Establishment> establishmentOptional = establishmentService.findById(id);
        if(!establishmentOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estacionamento não encontrado");
        }

        establishmentService.delete(establishmentOptional.get());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateEstablishment(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid EstablishmentDto establishmentDto) {
        Optional<Establishment> establishmentOptional = establishmentService.findById(id);
        if(!establishmentOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estacionamento não encontrado");
        }

        var establishmentModel = establishmentOptional.get();
        BeanUtils.copyProperties(establishmentDto, establishmentModel);
        establishmentModel.setName(establishmentDto.name().toUpperCase());
        establishmentModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.OK).body(establishmentService.save(establishmentModel));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneEstablishment(@PathVariable(value = "id") UUID id){
        Optional<Establishment> establishmentModelOptional = establishmentService.findById(id);
        return establishmentModelOptional.<ResponseEntity<Object>>map(establishment -> ResponseEntity.status(HttpStatus.OK).body(establishment)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estacionamento não encontrado."));
    }

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<Object> getEstablishmentByCnpj(@PathVariable(value = "cnpj") String cnpj) {
        Optional<Establishment> establishmentModelOptional = establishmentService.findByCnpj(cnpj);
        return establishmentModelOptional.<ResponseEntity<Object>>map(establishment -> ResponseEntity.status(HttpStatus.OK).body(establishment)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estacionamento não encontrado."));
    }
}
