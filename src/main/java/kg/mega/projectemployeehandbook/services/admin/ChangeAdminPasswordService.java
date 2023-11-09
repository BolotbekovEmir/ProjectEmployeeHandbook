package kg.mega.projectemployeehandbook.services.admin;

import kg.mega.projectemployeehandbook.errors.EditEntityException;
import kg.mega.projectemployeehandbook.models.dto.admin.ChangeAdminPasswordDTO;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;

public interface ChangeAdminPasswordService {

    RestResponse<EditEntityException> changeAdminPassword(ChangeAdminPasswordDTO changeAdminPasswordDTO);

}
