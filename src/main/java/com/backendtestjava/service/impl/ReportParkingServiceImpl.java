package com.backendtestjava.service.impl;

import com.backendtestjava.model.Parking;
import com.backendtestjava.service.EstablishmentService;
import com.backendtestjava.service.ParkingService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.backendtestjava.util.DateUtil.convertToLocalDateTime;
import static com.backendtestjava.util.DateUtil.formatDate;

@Service
@AllArgsConstructor
public class ReportParkingServiceImpl extends AbstractReportService {

    private final ParkingService parkingService;
    private final EstablishmentService establishmentService;

    public InputStreamResource report(UUID establishmentId, String dateTimeInitial, String dateTimeFinal) {
        var establishment = establishmentService.findById(establishmentId)
                .orElseThrow(() -> new IllegalArgumentException("Estacionamento não encontrado"));

        String title = "Relatório de Estacionamento - " + establishment.getName();

        List<String> columnHeaders = Arrays.asList("Veículo", "Placa", "Tipo", "Data/Hora Entrada", "Data/Hora Saída");

        List<Map<String, String>> rows = new ArrayList<>();

        LocalDateTime localDateTimeInitial = Objects.isNull(dateTimeInitial) ? null : convertToLocalDateTime(dateTimeInitial);
        LocalDateTime localDateTimeFinal = Objects.isNull(dateTimeFinal) ? null : convertToLocalDateTime(dateTimeFinal);
        List<Parking> parkingList = parkingService.findAllByEstablishment(establishment).stream()
                .filter(parking -> (localDateTimeInitial == null || parking.getEntryDateTime().isAfter(localDateTimeInitial) || parking.getEntryDateTime().isEqual(localDateTimeInitial))
                        && (localDateTimeFinal == null || parking.getEntryDateTime().isBefore(localDateTimeFinal) || parking.getEntryDateTime().isEqual(localDateTimeFinal)))
                .toList();

        for (Parking parking : parkingList) {
            Map<String, String> row = new HashMap<>();
            row.put("Veículo", parking.getVehicle().getModel());
            row.put("Placa", parking.getVehicle().getLicencePlate());
            row.put("Tipo", parking.getVehicle().getType().getDisplayName());
            row.put("Data/Hora Entrada", formatDate(parking.getEntryDateTime(), "dd/MM/yyyy hh:mm:ss"));
            row.put("Data/Hora Saída", parking.getExitDateTime() != null ? formatDate(parking.getExitDateTime(), "dd/MM/yyyy hh:mm:ss") : "Estacionado");

            rows.add(row);
        }

        Map<String, Long> summary = new HashMap<>();
        summary.put("Total de Entradas", (long) parkingList.size());
        summary.put("Total de Saídas", parkingList.stream().filter(parking -> parking.getExitDateTime() != null).count());

        return generateReport(title, columnHeaders, rows, summary);
    }

}
