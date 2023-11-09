package kg.mega.projectemployeehandbook.services.admin;

import kg.mega.projectemployeehandbook.models.dto.admin.GetAdminDTO;

import java.util.Set;

public interface SearchAdminService {

    Set<GetAdminDTO> searchAdmins(String searchField);

}
