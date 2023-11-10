package kg.mega.projectemployeehandbook.services.position.impl;

import kg.mega.projectemployeehandbook.errors.EditEntityException;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.position.EditPositionDTO;
import kg.mega.projectemployeehandbook.models.entities.Position;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;
import kg.mega.projectemployeehandbook.repositories.PositionRepository;
import kg.mega.projectemployeehandbook.services.log.LoggingService;
import kg.mega.projectemployeehandbook.services.position.EditPositionService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

import static java.lang.String.*;
import static lombok.AccessLevel.*;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class EditPositionServiceImpl implements EditPositionService {
    final PositionRepository positionRepository;
    final LoggingService     loggingService;

    @Override
    public RestResponse<EditEntityException> editPosition(EditPositionDTO editPositionDTO) {
        Position position = positionFindById(editPositionDTO.getEditedPositionId());

        updatePositionName(position, editPositionDTO.getNewPositionName());
        updatePositionMaster(position, editPositionDTO.getNewMasterId());
        disablePosition(position, editPositionDTO.isDisable());
        enablePosition(position, editPositionDTO.isEnable());

        positionRepository.save(position);

        loggingService.logInfo(
            format(InfoDescription.EDIT_POSITION_FORMAT, position.getId())
        );

        return new RestResponse<>(OK, null, OK.value(), new ArrayList<>());
    }

    private Position positionFindById(Long positionId) {
        Optional<Position> optionalPosition = positionRepository.findById(positionId);

        if (optionalPosition.isEmpty()) {
            throw new EditEntityException(ErrorDescription.POSITION_ID_NOT_FOUND);
        }

        return optionalPosition.orElseThrow(EditEntityException::new);
    }

    private void updatePositionName(Position position, String newPositionName) {
        if (!newPositionName.isEmpty()) {
            if (position.getPositionName().equals(newPositionName)) {
                throw new EditEntityException(ErrorDescription.POSITION_NAME_ALREADY_HAVE_THIS_NAME);
            } else {
                position.setPositionName(newPositionName);
            }
        }
    }

    private void updatePositionMaster(Position position, Long masterId) {
        if (masterId != null) {
            Position masterPosition = positionFindById(masterId);
            position.setMaster(masterPosition);
        }
    }

    private void disablePosition(Position position, boolean disable) {
        if (disable) {
            if (!position.getActive()) {
                throw new EditEntityException(ErrorDescription.POSITION_ALREADY_INACTIVE);
            } else {
                position.setActive(false);
            }
        }
    }

    private void enablePosition(Position position, boolean enable) {
        if (enable) {
            if (position.getActive()) {
                throw new EditEntityException(ErrorDescription.POSITION_ALREADY_ACTIVE);
            } else {
                position.setActive(true);
            }
        }
    }

}

