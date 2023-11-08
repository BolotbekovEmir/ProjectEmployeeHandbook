package kg.mega.projectemployeehandbook.services.admin;

import kg.mega.projectemployeehandbook.models.dto.GetAdminDTO;

import java.util.Set;

public interface SearchAdminService {

    Set<GetAdminDTO> searchAdmins(String searchField);

}
