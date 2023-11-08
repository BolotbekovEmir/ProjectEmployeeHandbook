package kg.mega.projectemployeehandbook.services.log.impl;

import kg.mega.projectemployeehandbook.services.log.LoggingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class LoggingServiceImpl implements LoggingService {
    private final LocalDateTime currentTime = LocalDateTime.now();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss");
    private final String formattedTime = currentTime.format(formatter);

    @Override
    public void logInfoWithTime(String message) {
        log.info("{} - {}", formattedTime, message);
    }

    @Override
    public void logErrorWithTime(String message) {
        log.error("{} - {}", formattedTime, message);
    }

}
