package kg.mega.projectemployeehandbook.services.admin;

import kg.mega.projectemployeehandbook.models.dto.admin.ChangeAdminPasswordDTO;

public interface ChangeAdminPasswordService {

    String changePassword(ChangeAdminPasswordDTO changeAdminPasswordDTO);
}
