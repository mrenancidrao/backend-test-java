package com.backendtestjava.controller;

import com.backendtestjava.model.Parking;
import com.backendtestjava.service.EstablishmentService;
import com.backendtestjava.service.ParkingService;
import com.backendtestjava.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final ParkingService parkingService;
    private final EstablishmentService establishmentService;

    @GetMapping("/parking/{establishmentId}")
    public ResponseEntity<InputStreamResource> exportParkingPdfReport(@PathVariable(value = "establishmentId") UUID establishmentId,
                                                                      @RequestParam(value = "dateTimeInitial", required = false) LocalDateTime dateTimeInitial,
                                                                      @RequestParam(value = "dateTimeFinal", required = false) LocalDateTime dateTimeFinal) {
        String title = "Relatório de Estacionamento";

        List<String> columnHeaders = Arrays.asList("Veículo", "Placa", "Tipo", "Data de Entrada", "Data de Saída");

        var establishment = establishmentService.findById(establishmentId)
                .orElseThrow(() -> new IllegalArgumentException("Estacionamento não encontrado"));

        List<Map<String, String>> rows = new ArrayList<>();
        List<Parking> parkingList = parkingService.findAllByEstablishment(establishment);

        for (Parking parking : parkingList) {
            Map<String, String> row = new HashMap<>();
            row.put("Veículo", parking.getVehicle().getModel());
            row.put("Placa", parking.getVehicle().getLicencePlate());
            row.put("Tipo", parking.getVehicle().getLicencePlate());
            row.put("Data de Entrada", parking.getEntryDateTime().toString());
            row.put("Data de Saída", parking.getExitDateTime() != null ? parking.getExitDateTime().toString() : "Estacionado");

            rows.add(row);
        }

        Map<String, Long> summary = new HashMap<>();
        summary.put("Total de Entradas", (long) parkingList.size());
        summary.put("Total de Saídas", (long) parkingList.size());

        InputStreamResource resource = reportService.generateReport(title, columnHeaders, rows, summary);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"parking_report.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}
