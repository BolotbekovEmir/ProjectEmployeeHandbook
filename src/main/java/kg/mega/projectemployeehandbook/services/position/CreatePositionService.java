package kg.mega.projectemployeehandbook.services.position;

import kg.mega.projectemployeehandbook.errors.CreateEntityException;
import kg.mega.projectemployeehandbook.models.dto.position.CreatePositionDTO;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;

public interface CreatePositionService {

    RestResponse<CreateEntityException> createPosition(CreatePositionDTO createPositionDTO);

}
