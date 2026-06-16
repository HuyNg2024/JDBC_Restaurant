package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Formats a LocalDateTime object to Vietnamese date-time string (dd/MM/yyyy HH:mm:ss)
     * @param dateTime The LocalDateTime to format
     * @return Formatted string
     */
    public String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        return dateTime.format(FORMATTER);
    }
}
