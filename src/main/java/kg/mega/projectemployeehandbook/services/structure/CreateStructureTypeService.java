package kg.mega.projectemployeehandbook.services.structure;

import kg.mega.projectemployeehandbook.errors.CreateEntityException;
import kg.mega.projectemployeehandbook.models.dto.structure.CreateStructureTypeDTO;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;

public interface CreateStructureTypeService {

    RestResponse<CreateEntityException> createStructureType(CreateStructureTypeDTO createStructureTypeDTO);

}
