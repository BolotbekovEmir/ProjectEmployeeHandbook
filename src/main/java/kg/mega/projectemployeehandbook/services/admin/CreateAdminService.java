package kg.mega.projectemployeehandbook.services.admin;

import kg.mega.projectemployeehandbook.errors.CreateAdminException;
import kg.mega.projectemployeehandbook.models.dto.CreateAdminDTO;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;

public interface CreateAdminService {

    RestResponse<CreateAdminException> createAdmin(CreateAdminDTO createAdminDTO);

}
