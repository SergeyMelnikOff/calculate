package org.example.controller;

import org.example.model.VacationPayResponse;
import org.example.service.VacationPayService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class VacationPayController {

    private final VacationPayService vacationPayService;

    public VacationPayController(VacationPayService vacationPayService) {
        this.vacationPayService = vacationPayService;
    }

    @GetMapping("/calculate")
    public ResponseEntity<?> calculateVacationPay(
            @RequestParam double averageSalary,
            @RequestParam(required = false) Integer vacationDays,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (!isValidParameters(vacationDays, startDate, endDate)) {
            return ResponseEntity.badRequest().body("Укажите либо дни отпуска, либо даты");
        }

        try {
            double amount = calculateAmount(averageSalary, vacationDays, startDate, endDate);
            return ResponseEntity.ok(new VacationPayResponse(amount));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Добавленный метод валидации параметров
    private boolean isValidParameters(Integer vacationDays, LocalDate startDate, LocalDate endDate) {
        return (vacationDays != null && startDate == null && endDate == null) ||
                (vacationDays == null && startDate != null && endDate != null);
    }

    private double calculateAmount(double averageSalary, Integer vacationDays,
                                   LocalDate startDate, LocalDate endDate) {
        if (vacationDays != null) {
            return vacationPayService.calculate(averageSalary, vacationDays);
        } else {
            return vacationPayService.calculate(averageSalary, startDate, endDate);
        }
    }
}