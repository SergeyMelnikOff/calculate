
import org.example.service.VacationPayService;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class VacationPayServiceTest {

    private final VacationPayService service = new VacationPayService();

    // Тесты для метода calculate(double averageSalary, int vacationDays)
    @Test
    void calculate_withValidSalaryAndDays_returnsCorrectAmount() {
        double result = service.calculate(29300, 29);
        double expected = (29300 / 29.3) * 29;
        assertEquals(expected, result, 0.01, "Некорректный расчет для 29 дней");
    }

    @Test
    void calculate_withZeroSalary_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> service.calculate(0, 10),
                "Должно быть исключение при нулевой зарплате");
    }

    @Test
    void calculate_withNegativeDays_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> service.calculate(10000, -5),
                "Должно быть исключение при отрицательных днях");
    }

    @Test
    void calculate_withMixedDays_returnsCorrectDays() {
        LocalDate start = LocalDate.of(2025, 5, 9);
        LocalDate end = LocalDate.of(2025, 5, 12);
        double expected = (29300 / 29.3) * 1; // Только 12 мая - рабочий
        double result = service.calculate(29300, start, end);
        assertEquals(expected, result, 0.01, "Некорректный подсчет рабочих дней");
    }

    @Test
    void calculate_singleWorkingDay_returnsCorrectAmount() {
        LocalDate date = LocalDate.of(2025, 2, 10);
        double expected = 29300 / 29.3;
        double result = service.calculate(29300, date, date);
        assertEquals(expected, result, 0.01, "Один рабочий день рассчитан неверно");
    }

    @Test
    void calculate_withInvalidDateOrder_throwsException() {
        LocalDate start = LocalDate.of(2025, 1, 2);
        LocalDate end = LocalDate.of(2025, 1, 1);
        assertThrows(IllegalArgumentException.class, () -> service.calculate(10000, start, end),
                "Должно быть исключение при некорректном порядке дат");
    }

    @Test
    void calculate_withNullDates_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> service.calculate(10000, null, LocalDate.now()),
                "Null startDate должно вызывать исключение");
        assertThrows(IllegalArgumentException.class, () -> service.calculate(10000, LocalDate.now(), null),
                "Null endDate должно вызывать исключение");
    }

    @Test
    void calculate_standardWorkWeek_returnsFiveDays() {
        LocalDate start = LocalDate.of(2025, 2, 10); // Понедельник
        LocalDate end = LocalDate.of(2025, 2, 14);   // Пятница
        double expected = (29300 / 29.3) * 5;
        double result = service.calculate(29300, start, end);
        assertEquals(expected, result, 0.01, "Стандартная рабочая неделя рассчитана неверно");
    }

    @Test
    void calculate_holidayInMiddleOfPeriod_excluded() {
        LocalDate start = LocalDate.of(2025, 5, 8);  // Рабочий день
        LocalDate end = LocalDate.of(2025, 5, 12);   // Включает 9 мая (праздник)
        // 8, 12 - рабочие, 9-11 - праздник и выходные
        double expected = (29300 / 29.3) * 2;
        double result = service.calculate(29300, start, end);
        assertEquals(expected, result, 0.01, "Праздник в середине периода должен быть исключен");
    }
}