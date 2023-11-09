package kg.mega.projectemployeehandbook.services.admin;

import kg.mega.projectemployeehandbook.errors.EditEntityException;
import kg.mega.projectemployeehandbook.models.dto.admin.EditAdminDTO;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;

public interface EditAdminService {

    RestResponse<EditEntityException> editAdmin(EditAdminDTO editAdminDTO);

}
