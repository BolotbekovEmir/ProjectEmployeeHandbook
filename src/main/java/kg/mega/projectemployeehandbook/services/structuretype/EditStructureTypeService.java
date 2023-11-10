package kg.mega.projectemployeehandbook.services.structuretype;

import kg.mega.projectemployeehandbook.errors.EditEntityException;
import kg.mega.projectemployeehandbook.models.dto.structuretype.EditStructureTypeDTO;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;

public interface EditStructureTypeService {

    RestResponse<EditEntityException> editStructureType(EditStructureTypeDTO editStructureTypeDTO);

}
