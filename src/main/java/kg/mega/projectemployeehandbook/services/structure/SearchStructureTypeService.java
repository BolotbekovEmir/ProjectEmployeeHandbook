package kg.mega.projectemployeehandbook.services.structure;

import kg.mega.projectemployeehandbook.models.dto.structure.GetStructureTypeDTO;

import java.util.Set;

public interface SearchStructureTypeService {

    Set<GetStructureTypeDTO> searchStructureType(String searchField);

}
