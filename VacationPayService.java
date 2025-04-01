package org.example.service;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class VacationPayService {

    private static final double AVERAGE_MONTHLY_DAYS = 29.3;

    public double calculate(double averageSalary, int vacationDays) {
        validateInput(averageSalary, vacationDays);
        return (averageSalary / AVERAGE_MONTHLY_DAYS) * vacationDays;
    }

    public double calculate(double averageSalary, LocalDate startDate, LocalDate endDate) {
        validateDates(startDate, endDate);
        int workingDays = calculateWorkingDays(startDate, endDate);
        return calculate(averageSalary, workingDays);
    }

    private int calculateWorkingDays(LocalDate startDate, LocalDate endDate) {
        int workingDays = 0;
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            if (!isWeekend(currentDate) && !isHoliday(currentDate)) {
                workingDays++;
            }
            currentDate = currentDate.plusDays(1);
        }

        return workingDays;
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    private boolean isHoliday(LocalDate date) {
        Set<LocalDate> holidays = new HashSet<>();
        // Примеры праздничных дней для 2025 года
        holidays.add(LocalDate.of(2025, 1, 1));
        holidays.add(LocalDate.of(2025, 1, 2));
        holidays.add(LocalDate.of(2025, 1, 3));
        holidays.add(LocalDate.of(2025, 1, 4));
        holidays.add(LocalDate.of(2025, 1, 5));
        holidays.add(LocalDate.of(2025, 1, 6));
        holidays.add(LocalDate.of(2025, 1, 7));
        holidays.add(LocalDate.of(2025, 1, 8));
        holidays.add(LocalDate.of(2025, 2, 23));
        holidays.add(LocalDate.of(2025, 3, 8));
        holidays.add(LocalDate.of(2025, 5, 1));
        holidays.add(LocalDate.of(2025, 5, 9));
        holidays.add(LocalDate.of(2025, 6, 12));
        holidays.add(LocalDate.of(2025, 11, 4));

        return holidays.contains(date);
    }

    private void validateInput(double averageSalary, int vacationDays) {
        if (averageSalary <= 0 || vacationDays <= 0) {
            throw new IllegalArgumentException("Некорректные входные данные");
        }
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Даты должны быть указаны");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Начальная дата позже конечной");
        }
    }
}