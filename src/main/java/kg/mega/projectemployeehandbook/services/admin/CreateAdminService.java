package kg.mega.projectemployeehandbook.services.admin;

import kg.mega.projectemployeehandbook.errors.CreateEntityException;
import kg.mega.projectemployeehandbook.models.dto.admin.CreateAdminDTO;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;

public interface CreateAdminService {

    RestResponse<CreateEntityException> createAdmin(CreateAdminDTO createAdminDTO);

}
