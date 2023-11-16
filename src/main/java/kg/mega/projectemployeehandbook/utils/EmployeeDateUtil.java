package kg.mega.projectemployeehandbook.utils;

import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.services.ErrorCollectorService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static lombok.AccessLevel.*;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EmployeeDateUtil {
    ErrorCollectorService errorCollectorService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public LocalDate parseDate(String date) {
        return date.isBlank()
            ? null
            : LocalDate.parse(date, formatter);
    }

    public LocalDate parseOrNow(String date) {
        return date.isBlank()
            ? LocalDate.now()
            : LocalDate.parse(date, formatter);
    }

    public void validateDatePair(String startDate, String endDate) {
        if (!endDate.isBlank() && startDate.isBlank()) {
            errorCollectorService.addErrorMessages(
                List.of(
                    String.format(ErrorDescription.END_DATE_IS_PRESENT_BUT_START_DATE_IS_NULL_FORMAT, endDate)
                )
            );
        }
    }
}