package kg.mega.projectemployeehandbook.services.position.impl;

import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.position.EditPositionDTO;
import kg.mega.projectemployeehandbook.models.entities.Position;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.repositories.PositionRepository;
import kg.mega.projectemployeehandbook.errors.ErrorCollector;
import kg.mega.projectemployeehandbook.services.log.LoggingService;
import kg.mega.projectemployeehandbook.services.position.EditPositionService;
import kg.mega.projectemployeehandbook.utils.CommonRepositoryUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static java.lang.String.*;
import static java.util.List.*;
import static lombok.AccessLevel.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EditPositionServiceImpl implements EditPositionService {
    PositionRepository positionRepository;

    LoggingService loggingService;

    CommonRepositoryUtil commonRepositoryUtil;
    ErrorCollector       errorCollector;

    @Override
    @Transactional
    public String editPosition(EditPositionDTO editPositionDTO) {
        errorCollector.cleanup();

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
        loggingService.logInfo(operationSuccessMessage);
        return operationSuccessMessage;
    }

    private boolean validateEditPosition(EditPositionDTO editPositionDTO, Position position) {
        boolean valid = true;
        valid &= checkAndSetNewPositionMaster(editPositionDTO.getNewMasterId(), position);
        valid &= checkAndSetNewPositionName(editPositionDTO.getNewPositionName(), position);
        valid &= checkAndSetNewPositionActive(editPositionDTO.isDisable(), editPositionDTO.isEnable(), position);
        return valid;
    }

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
            position.setMaster(masterPosition);
            return true;
        }
    }

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
            position.setPositionName(newPositionName);
            return true;
        }
    }

    private boolean checkAndSetNewPositionActive(boolean disable, boolean enable, Position position) {
        if (disable) {
            if (!position.getActive()) {
                errorCollector.addErrorMessages(
                    of(ErrorDescription.POSITION_ALREADY_INACTIVE)
                );
                return false;
            } else {
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
                position.setActive(true);
                return true;
            }
        }
        return true;
    }
}