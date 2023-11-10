package kg.mega.projectemployeehandbook.services.position;

import kg.mega.projectemployeehandbook.errors.EditEntityException;
import kg.mega.projectemployeehandbook.models.dto.position.EditPositionDTO;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;

public interface EditPositionService {

    RestResponse<EditEntityException> editPosition(EditPositionDTO editPositionDTO);

}
