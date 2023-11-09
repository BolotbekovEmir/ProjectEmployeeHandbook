package kg.mega.projectemployeehandbook.services.structure;

import kg.mega.projectemployeehandbook.errors.EditEntityException;
import kg.mega.projectemployeehandbook.models.dto.structure.EditStructureTypeDTO;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;

public interface EditStructureTypeService {

    RestResponse<EditEntityException> editStructureType(EditStructureTypeDTO editStructureTypeDTO);

}
