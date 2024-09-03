package com.backendtestjava.controller;

import com.backendtestjava.model.Parking;
import com.backendtestjava.service.ParkingService;
import com.backendtestjava.service.impl.ReportParkingServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    private final ReportParkingServiceImpl reportParkingService;

    @GetMapping("/parking/{establishmentId}")
    public ResponseEntity<Object> exportParkingPdfReport(@PathVariable(value = "establishmentId") UUID establishmentId,
                                                                      @RequestParam(value = "dateTimeInitial", required = false) String dateTimeInitial,
                                                                      @RequestParam(value = "dateTimeFinal", required = false) String dateTimeFinal) {

        try {
            InputStreamResource resource = reportParkingService.report(establishmentId, dateTimeInitial, dateTimeFinal);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"parking_report.pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
