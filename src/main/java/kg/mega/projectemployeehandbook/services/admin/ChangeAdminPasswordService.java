package kg.mega.projectemployeehandbook.services.admin;

import kg.mega.projectemployeehandbook.errors.EditAdminException;
import kg.mega.projectemployeehandbook.models.dto.ChangeAdminPasswordDTO;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;

public interface ChangeAdminPasswordService {

    RestResponse<EditAdminException> changeAdminPassword(ChangeAdminPasswordDTO changeAdminPasswordDTO);

}
