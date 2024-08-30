package com.backendtestjava.service.impl;

import com.backendtestjava.model.Parking;
import com.backendtestjava.service.EstablishmentService;
import com.backendtestjava.service.ParkingService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class ReportParkingServiceImpl extends AbstractReportService {

    private final ParkingService parkingService;
    private final EstablishmentService establishmentService;

    public InputStreamResource report(UUID establishmentId, LocalDateTime dateTimeInitial, LocalDateTime dateTimeFinal) {
        var establishment = establishmentService.findById(establishmentId)
                .orElseThrow(() -> new IllegalArgumentException("Estacionamento não encontrado"));

        String title = "Relatório de Estacionamento - " + establishment.getName();

        List<String> columnHeaders = Arrays.asList("Veículo", "Placa", "Tipo", "Data de Entrada", "Data de Saída");

        List<Map<String, String>> rows = new ArrayList<>();
        List<Parking> parkingList = parkingService.findAllByEstablishment(establishment);

        for (Parking parking : parkingList) {
            Map<String, String> row = new HashMap<>();
            row.put("Veículo", parking.getVehicle().getModel());
            row.put("Placa", parking.getVehicle().getLicencePlate());
            row.put("Tipo", parking.getVehicle().getType().getDisplayName());
            row.put("Data de Entrada", parking.getEntryDateTime().toString());
            row.put("Data de Saída", parking.getExitDateTime() != null ? parking.getExitDateTime().toString() : "Estacionado");

            rows.add(row);
        }

        Map<String, Long> summary = new HashMap<>();
        summary.put("Total de Entradas", (long) parkingList.size());
        summary.put("Total de Saídas", parkingList.stream().filter(parking -> parking.getExitDateTime() != null).count());

        return generateReport(title, columnHeaders, rows, summary);
    }

}
