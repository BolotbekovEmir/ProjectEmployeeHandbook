package kg.mega.projectemployeehandbook.services.admin;

import kg.mega.projectemployeehandbook.errors.EditAdminException;
import kg.mega.projectemployeehandbook.models.dto.EditAdminDTO;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;

public interface EditAdminService {

    RestResponse<EditAdminException> editAdmin(EditAdminDTO editAdminDTO);

}
