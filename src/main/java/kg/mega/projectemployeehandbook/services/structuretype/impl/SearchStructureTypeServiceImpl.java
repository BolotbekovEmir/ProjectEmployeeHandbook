package kg.mega.projectemployeehandbook.services.structuretype.impl;

import kg.mega.projectemployeehandbook.configuration.MapperConfiguration;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.models.dto.structuretype.GetStructureTypeDTO;
import kg.mega.projectemployeehandbook.models.entities.StructureType;
import kg.mega.projectemployeehandbook.repositories.StructureTypeRepository;
import kg.mega.projectemployeehandbook.services.structuretype.SearchStructureTypeService;
import kg.mega.projectemployeehandbook.utils.CommonRepositoryUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static lombok.AccessLevel.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SearchStructureTypeServiceImpl implements SearchStructureTypeService {
    StructureTypeRepository structureTypeRepository;

    CommonRepositoryUtil commonRepositoryUtil;
    MapperConfiguration  mapper;

    @Override
    public Set<GetStructureTypeDTO> searchStructureType(String searchField) {
        if (searchField.isBlank()) {
            return mapResult(structureTypeRepository.findAllByActiveIsTrue());
        }

        Set<StructureType>
            structureTypesByName = structureTypeRepository.findAllByStructureTypeNameContainsIgnoreCaseAndActiveIsTrue(searchField),
            resultSearch = new HashSet<>(structureTypesByName);

        try {
            Long structureTypeId = Long.parseLong(searchField);
            StructureType structureTypeFindById = commonRepositoryUtil.getEntityById(
                structureTypeId,
                structureTypeRepository,
                ErrorDescription.STRUCTURE_TYPE_ID_NOT_FOUND
            );
            resultSearch.add(structureTypeFindById);
        } catch (NumberFormatException e) {
            // Исключение игнорируется, так как searchField не является ID.
        }

        return mapResult(resultSearch);
    }

    private Set<GetStructureTypeDTO> mapResult(Set<StructureType> structureTypes) {
        return structureTypes.stream()
            .map(e -> mapper.getMapper()
                .map(e, GetStructureTypeDTO.class))
            .collect(Collectors.toSet());
    }

}
