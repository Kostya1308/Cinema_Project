package by.academy.final_project.formatters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class MyFormatterGreenForConsolle extends Formatter {

    @Override
    public String format(LogRecord record) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        return (char)27 + "[32m" + LocalDateTime.now().format(dateTimeFormatter) + " - " + record.getMessage() + "\n";
    }
}
