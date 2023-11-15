package kg.mega.projectemployeehandbook.services.admin;

import kg.mega.projectemployeehandbook.models.dto.admin.AuthAdminDTO;
import kg.mega.projectemployeehandbook.models.dto.admin.TokenAdminDTO;

public interface AuthAdminService {

    TokenAdminDTO adminAuth(AuthAdminDTO authAdminDTO);

}
