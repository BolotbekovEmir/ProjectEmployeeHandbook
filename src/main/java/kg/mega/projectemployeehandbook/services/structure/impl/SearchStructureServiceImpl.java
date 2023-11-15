package kg.mega.projectemployeehandbook.services.structure.impl;

import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.models.entities.Structure;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.repositories.StructureRepository;
import kg.mega.projectemployeehandbook.services.structure.SearchStructureService;
import kg.mega.projectemployeehandbook.utils.CommonRepositoryUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SearchStructureServiceImpl implements SearchStructureService {
    CommonRepositoryUtil commonRepositoryUtil;
    StructureRepository  structureRepository;

    @Override
    public Set<String> searchStructure(String searchField) {
        Set<Structure> structures = structureRepository.findAllByStructureNameContainsIgnoreCaseAndActiveIsTrue(searchField);

        try {
            Long structureId = Long.parseLong(searchField);
            Structure structure = commonRepositoryUtil.getEntityById(
                structureId,
                structureRepository,
                ErrorDescription.STRUCTURE_ID_NOT_FOUND,
                ExceptionType.GET_ENTITY_EXCEPTION
            );
            structures.add(structure);
        } catch (NumberFormatException exception) {
            // Исключение игнорируется, так как searchField не содержит id.
        }

        return structureParses(structures);
    }

    private Set<String> structureParses(Set<Structure> structures) {
        return structures.stream()
            .map(structure -> {
                StringBuilder resultParse = new StringBuilder();
                if (structure.getMaster() != null) {
                    resultParse.append(structure.getStructureName());
                    do {
                        resultParse
                            .append(" / ")
                            .append(structure.getMaster().getStructureName());
                        structure = structure.getMaster();
                    } while (structure.getMaster() != null);
                } else {
                    resultParse.append(structure.getStructureName());
                }
                resultParse.append(" (CEO)");
                return resultParse.toString();
            })
            .collect(Collectors.toSet());
    }
}