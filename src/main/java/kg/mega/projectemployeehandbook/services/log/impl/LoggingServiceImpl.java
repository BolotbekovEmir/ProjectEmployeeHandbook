package kg.mega.projectemployeehandbook.services.log.impl;

import kg.mega.projectemployeehandbook.services.log.LoggingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoggingServiceImpl implements LoggingService {
    @Override
    public void logError(String message) {
        log.error(message);
    }
}