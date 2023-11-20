package kg.mega.projectemployeehandbook.services.position.impl;

import kg.mega.projectemployeehandbook.errors.ErrorCollector;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.position.CreatePositionDTO;
import kg.mega.projectemployeehandbook.models.entities.Position;
import kg.mega.projectemployeehandbook.repositories.PositionRepository;
import kg.mega.projectemployeehandbook.services.log.InfoCollector;
import kg.mega.projectemployeehandbook.services.position.CreatePositionService;
import kg.mega.projectemployeehandbook.utils.CommonRepositoryUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;

/**
 * Сервис для создания позиции.
 * */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CreatePositionServiceImpl implements CreatePositionService {
    PositionRepository positionRepository;

    CommonRepositoryUtil commonRepositoryUtil;
    ErrorCollector       errorCollector;
    InfoCollector        infoCollector;

    /**
     * Создает новую должность на основе предоставленных данных.
     *
     * @param createPositionDTO объект с данными для создания должности
     * @return сообщение об успешном выполнении операции создания должности
     */
    @Override
    public String createPosition(CreatePositionDTO createPositionDTO) {
        errorCollector.cleanup();
        infoCollector.cleanup();

        Position
            position = new Position(),
            masterPosition = commonRepositoryUtil.getEntityById(
                createPositionDTO.getMasterId(),
                positionRepository,
                ErrorDescription.POSITION_ID_NOT_FOUND
            );

        position.setPositionName(createPositionDTO.getPositionName());
        position.setMaster(masterPosition);
        position.setActive(createPositionDTO.getActive());

        positionRepository.save(position);

        String operationSuccessMessage = format(InfoDescription.CREATE_POSITION_FORMAT, position.getId());

        infoCollector.setChangerInfo();
        infoCollector.setEntityInfo(position.getId(), position.getPositionName());
        infoCollector.writeEntityLog(operationSuccessMessage);

        return operationSuccessMessage;
    }
}
