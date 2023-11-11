package kg.mega.projectemployeehandbook.services.position.impl;

import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.position.CreatePositionDTO;
import kg.mega.projectemployeehandbook.models.entities.Position;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.repositories.PositionRepository;
import kg.mega.projectemployeehandbook.services.ErrorCollectorService;
import kg.mega.projectemployeehandbook.services.log.LoggingService;
import kg.mega.projectemployeehandbook.services.position.CreatePositionService;
import kg.mega.projectemployeehandbook.utils.CommonRepositoryUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class CreatePositionServiceImpl implements CreatePositionService {
    final ErrorCollectorService errorCollectorService;
    final CommonRepositoryUtil  commonRepositoryUtil;
    final PositionRepository    positionRepository;
    final LoggingService        loggingService;

    @Override
    public String createPosition(CreatePositionDTO createPositionDTO) {
        errorCollectorService.cleanup();

        Position
            position = new Position(),
            masterPosition = commonRepositoryUtil.getEntityById(
                createPositionDTO.getMasterId(),
                positionRepository,
                ErrorDescription.POSITION_ID_NOT_FOUND,
                ExceptionType.CREATE_ENTITY_EXCEPTION
            );

        position.setPositionName(createPositionDTO.getPositionName());
        position.setMaster(masterPosition);
        position.setActive(createPositionDTO.isActive());

        positionRepository.save(position);

        String successfulResultMessage = format(InfoDescription.CREATE_POSITION_FORMAT, position.getId());
        loggingService.logInfo(successfulResultMessage);
        return successfulResultMessage;
    }
}
