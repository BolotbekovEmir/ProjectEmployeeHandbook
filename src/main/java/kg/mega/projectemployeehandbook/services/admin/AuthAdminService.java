package kg.mega.projectemployeehandbook.services.admin;

import kg.mega.projectemployeehandbook.models.dto.AuthAdminDTO;
import kg.mega.projectemployeehandbook.models.dto.TokenAdminDTO;

public interface AuthAdminService {

    TokenAdminDTO adminAuth(AuthAdminDTO authAdminDTO);

}
