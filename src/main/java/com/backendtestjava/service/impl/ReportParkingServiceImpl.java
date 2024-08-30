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

        return generateReport(title, columnHeaders, rows, summary);
    }

}
