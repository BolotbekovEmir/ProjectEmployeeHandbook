package kg.mega.projectemployeehandbook.services.structure.impl;

import kg.mega.projectemployeehandbook.configuration.MapperConfiguration;
import kg.mega.projectemployeehandbook.errors.GetEntityException;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.models.dto.structure.GetStructureTypeDTO;
import kg.mega.projectemployeehandbook.models.entities.StructureType;
import kg.mega.projectemployeehandbook.repositories.StructureTypeRepository;
import kg.mega.projectemployeehandbook.services.structure.SearchStructureTypeService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static lombok.AccessLevel.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class SearchStructureTypeServiceImpl implements SearchStructureTypeService {
    final StructureTypeRepository structureTypeRepository;
    final MapperConfiguration     mapper;

    @Override
    public Set<GetStructureTypeDTO> searchStructureType(String searchField) {

        Set<StructureType>
            structureTypesByName = structureTypeRepository.findAllByStructureTypeNameContainsIgnoreCaseAndActiveIsTrue(searchField),
            resultSearch = new HashSet<>(structureTypesByName);

        try {
            Long structureTypeId = Long.parseLong(searchField);
            StructureType structureTypeFindById = structureTypeRepository.findById(structureTypeId).orElseThrow(
                () -> new GetEntityException(ErrorDescription.STRUCTURE_TYPE_ID_NOT_FOUND)
            );
            resultSearch.add(structureTypeFindById);
        } catch (NumberFormatException e) {
            // Игнорируется исключение, так как searchField не является числом.
        }

        return resultSearch.stream().map(e -> mapper.getMapper().map(e, GetStructureTypeDTO.class)).collect(Collectors.toSet());
    }
}
