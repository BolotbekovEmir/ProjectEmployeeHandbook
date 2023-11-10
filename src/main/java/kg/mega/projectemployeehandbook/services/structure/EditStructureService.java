package kg.mega.projectemployeehandbook.services.structure;

import kg.mega.projectemployeehandbook.errors.EditEntityException;
import kg.mega.projectemployeehandbook.models.dto.structure.EditStructureDTO;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;

public interface EditStructureService {

    RestResponse<EditEntityException> editStructure(EditStructureDTO editStructureDTO);

}
