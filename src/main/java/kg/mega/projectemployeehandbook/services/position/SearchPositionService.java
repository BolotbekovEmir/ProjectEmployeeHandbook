package kg.mega.projectemployeehandbook.services.position;

import kg.mega.projectemployeehandbook.models.dto.GetPositionDTO;

import java.util.Set;

public interface SearchPositionService {

    Set<GetPositionDTO> searchPosition(String searchField);

}
