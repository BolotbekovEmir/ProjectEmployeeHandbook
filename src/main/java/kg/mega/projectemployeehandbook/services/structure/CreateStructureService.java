package kg.mega.projectemployeehandbook.services.structure;

import kg.mega.projectemployeehandbook.errors.CreateEntityException;
import kg.mega.projectemployeehandbook.models.dto.structure.CreateStructureDTO;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;

public interface CreateStructureService {

    RestResponse<CreateEntityException> createStructure(CreateStructureDTO createStructureDTO);

}
