package kg.mega.projectemployeehandbook.services.structuretype;

import kg.mega.projectemployeehandbook.models.dto.structuretype.GetStructureTypeDTO;

import java.util.Set;

public interface SearchStructureTypeService {
    Set<GetStructureTypeDTO> searchStructureType(String searchField);
}
