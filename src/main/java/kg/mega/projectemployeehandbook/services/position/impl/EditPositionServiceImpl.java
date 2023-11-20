package kg.mega.projectemployeehandbook.services.position.impl;

import kg.mega.projectemployeehandbook.errors.ErrorCollector;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.position.EditPositionDTO;
import kg.mega.projectemployeehandbook.models.entities.Position;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.repositories.PositionRepository;
import kg.mega.projectemployeehandbook.services.log.InfoCollector;
import kg.mega.projectemployeehandbook.services.position.EditPositionService;
import kg.mega.projectemployeehandbook.utils.CommonRepositoryUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static java.lang.String.format;
import static java.util.List.of;
import static lombok.AccessLevel.PRIVATE;

/**
 * Сервисный класс для внесения изменений в информацию о должностях.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EditPositionServiceImpl implements EditPositionService {
    PositionRepository positionRepository;

    CommonRepositoryUtil commonRepositoryUtil;
    ErrorCollector       errorCollector;
    InfoCollector        infoCollector;

    /**
     * Редактирует информацию о должности на основе переданных данных.
     * @param editPositionDTO Данные для редактирования должности.
     * @return Сообщение об успешном завершении операции.
     */
    @Override
    @Transactional
    public String editPosition(EditPositionDTO editPositionDTO) {
        errorCollector.cleanup();
        infoCollector.cleanup();

        Position position = commonRepositoryUtil.getEntityById(
            editPositionDTO.getEditedPositionId(),
            positionRepository,
            ErrorDescription.POSITION_ID_NOT_FOUND
        );

        if (!validateEditPosition(editPositionDTO, position)) {
            errorCollector.callException(ExceptionType.EDIT_ENTITY_EXCEPTION);
        }

        positionRepository.save(position);

        String operationSuccessMessage = format(InfoDescription.EDIT_POSITION_FORMAT, position.getId());
        infoCollector.setChangerInfo();
        infoCollector.setEntityInfo(position.getId(), position.getPositionName());
        infoCollector.writeFullLog(operationSuccessMessage);

        return operationSuccessMessage;
    }

    /**
     * Проверяет валидность изменений в данных о должности на основе переданных параметров.
     * @param editPositionDTO Данные для редактирования должности.
     * @param position Объект текущей должности.
     * @return True, если все изменения прошли проверку, в противном случае - false.
     */
    private boolean validateEditPosition(EditPositionDTO editPositionDTO, Position position) {
        boolean valid = true;
        valid &= checkAndSetNewPositionMaster(editPositionDTO.getNewMasterId(), position);
        valid &= checkAndSetNewPositionName(editPositionDTO.getNewPositionName(), position);
        valid &= checkAndSetNewPositionActive(editPositionDTO.getDisable(), editPositionDTO.getEnable(), position);
        return valid;
    }

    /**
     * Проверяет и устанавливает новую должность-руководителя, если она была изменена.
     * @param newMasterId Новый идентификатор должности-руководителя.
     * @param position Объект текущей должности.
     * @return True, если операция выполнена успешно, в противном случае - false.
     */
    private boolean checkAndSetNewPositionMaster(Long newMasterId, Position position) {
        if (newMasterId == null) {
            return true;
        }

        Position masterPosition = commonRepositoryUtil.getEntityById(
            newMasterId,
            positionRepository,
            ErrorDescription.POSITION_ID_NOT_FOUND
        );

        if (Objects.equals(position.getMaster(), masterPosition)) {
            errorCollector.addErrorMessages(
                of(ErrorDescription.POSITION_ALREADY_THIS_MASTER)
            );
            return false;
        } else {
            infoCollector.addFieldUpdatesInfo(
                    "master",
                    position.getMaster().getId().toString(),
                    newMasterId.toString()
            );
            position.setMaster(masterPosition);
            return true;
        }
    }

    /**
     * Проверяет и устанавливает новое название должности, если оно было изменено.
     * @param newPositionName Новое название должности.
     * @param position Объект текущей должности.
     * @return True, если операция выполнена успешно, в противном случае - false.
     */
    private boolean checkAndSetNewPositionName(String newPositionName, Position position) {
        if (newPositionName.isBlank()) {
            return true;
        }

        if (newPositionName.equals(position.getPositionName())) {
            errorCollector.addErrorMessages(
                of(ErrorDescription.POSITION_ALREADY_THIS_NAME)
            );
            return false;
        } else {
            infoCollector.addFieldUpdatesInfo(
                    "positionName",
                    position.getPositionName(),
                    newPositionName
            );
            position.setPositionName(newPositionName);
            return true;
        }
    }

    /**
     * Проверяет и устанавливает новый статус активности должности, если он был изменен.
     * @param disable Флаг для деактивации должности.
     * @param enable Флаг для активации должности.
     * @param position Объект текущей должности.
     * @return True, если операция выполнена успешно, в противном случае - false.
     */
    private boolean checkAndSetNewPositionActive(boolean disable, boolean enable, Position position) {
        if (disable) {
            if (!position.getActive()) {
                errorCollector.addErrorMessages(
                    of(ErrorDescription.POSITION_ALREADY_INACTIVE)
                );
                return false;
            } else {
                infoCollector.addFieldUpdatesInfo("active", "true", "false");
                position.setActive(false);
                return true;
            }
        }
        if (enable) {
            if (position.getActive()) {
                errorCollector.addErrorMessages(
                    of(ErrorDescription.POSITION_ALREADY_ACTIVE)
                );
                return false;
            } else {
                infoCollector.addFieldUpdatesInfo("active", "false", "true");
                position.setActive(true);
                return true;
            }
        }
        return true;
    }
}