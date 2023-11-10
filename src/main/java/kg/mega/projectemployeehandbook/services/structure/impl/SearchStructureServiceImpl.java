package kg.mega.projectemployeehandbook.services.structure.impl;

import kg.mega.projectemployeehandbook.errors.GetEntityException;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.models.dto.structure.OrganizationStructureDTO;
import kg.mega.projectemployeehandbook.models.entities.Structure;
import kg.mega.projectemployeehandbook.repositories.StructureRepository;
import kg.mega.projectemployeehandbook.services.structure.SearchStructureService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class SearchStructureServiceImpl implements SearchStructureService {
    final StructureRepository structureRepository;

    @Override
    public OrganizationStructureDTO searchStructure(Long structureId) {
        Structure structure = findById(structureId);
        StringBuilder resultBody = new StringBuilder();

        if (structure.getMaster() != null) {

            resultBody.append(structure.getStructureName());

            do {
                resultBody
                    .append(" / ")
                    .append(structure.getMaster().getStructureName());

                structure = structure.getMaster();

            } while (structure.getMaster() != null);

        } else {
            resultBody.append(structure.getStructureName());
        }

        resultBody.append(" (CEO)");

        return new OrganizationStructureDTO(resultBody.toString());
    }

    private Structure findById(Long structureId) {
        Optional<Structure> optionalStructure = structureRepository.findById(structureId);

        if (optionalStructure.isEmpty()) {
            throw new GetEntityException(ErrorDescription.STRUCTURE_ID_NOT_FOUND);
        }

        return optionalStructure.orElseThrow(GetEntityException::new);
    }
}