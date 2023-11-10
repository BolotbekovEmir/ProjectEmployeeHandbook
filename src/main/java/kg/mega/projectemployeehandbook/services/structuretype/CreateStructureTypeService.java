package kg.mega.projectemployeehandbook.services.structuretype;

import kg.mega.projectemployeehandbook.errors.CreateEntityException;
import kg.mega.projectemployeehandbook.models.dto.structuretype.CreateStructureTypeDTO;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;

public interface CreateStructureTypeService {

    RestResponse<CreateEntityException> createStructureType(CreateStructureTypeDTO createStructureTypeDTO);

}
