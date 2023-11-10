package kg.mega.projectemployeehandbook.services.position.impl;

import kg.mega.projectemployeehandbook.errors.CreateEntityException;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.position.CreatePositionDTO;
import kg.mega.projectemployeehandbook.models.entities.Position;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;
import kg.mega.projectemployeehandbook.repositories.PositionRepository;
import kg.mega.projectemployeehandbook.services.log.LoggingService;
import kg.mega.projectemployeehandbook.services.position.CreatePositionService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

import static java.lang.String.*;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class CreatePositionServiceImpl implements CreatePositionService {
    final PositionRepository positionRepository;
    final LoggingService     loggingService;

    @Override
    public RestResponse<CreateEntityException> createPosition(CreatePositionDTO createPositionDTO) {
        Position
            position = new Position(),
            masterPosition = positionFindById(createPositionDTO.getMasterId());

        position.setPositionName(createPositionDTO.getPositionName());
        position.setMaster(masterPosition);
        position.setActive(createPositionDTO.isActive());

        positionRepository.save(position);

        loggingService.logInfo(
            format(InfoDescription.CREATE_POSITION_FORMAT, position.getId())
        );

        return new RestResponse<>(CREATED, null, CREATED.value(), new ArrayList<>());
    }

    private Position positionFindById(long positionId) {
        Optional<Position> optionalPosition = positionRepository.findById(positionId);

        if (optionalPosition.isEmpty()) {
            throw new CreateEntityException(ErrorDescription.POSITION_ID_NOT_FOUND);
        }

        return optionalPosition.orElseThrow(CreateEntityException::new);
    }

}
