package kg.mega.projectemployeehandbook.services.structure.impl;

import kg.mega.projectemployeehandbook.errors.CreateEntityException;
import kg.mega.projectemployeehandbook.errors.EditEntityException;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.structure.CreateStructureDTO;
import kg.mega.projectemployeehandbook.models.entities.Structure;
import kg.mega.projectemployeehandbook.models.entities.StructureType;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;
import kg.mega.projectemployeehandbook.repositories.StructureRepository;
import kg.mega.projectemployeehandbook.repositories.StructureTypeRepository;
import kg.mega.projectemployeehandbook.services.log.LoggingService;
import kg.mega.projectemployeehandbook.services.structure.CreateStructureService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.lang.String.*;
import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class CreateStructureServiceImpl implements CreateStructureService {
    final StructureTypeRepository structureTypeRepository;
    final StructureRepository     structureRepository;
    final LoggingService          loggingService;

    final RestResponse<CreateEntityException> response = new RestResponse<>();

    @Override
    public RestResponse<CreateEntityException> createStructure(CreateStructureDTO createStructureDTO) {
        Structure structureMaster = findById(
                createStructureDTO.getMasterId(), structureRepository, ErrorDescription.STRUCTURE_ID_NOT_FOUND
        );

        StructureType structureType = findById(
            createStructureDTO.getStructureTypeId(), structureTypeRepository, ErrorDescription.STRUCTURE_TYPE_ID_NOT_FOUND
        );

        Structure structure = new Structure(
            structureMaster,
            structureType,
            createStructureDTO.getStructureName(),
            createStructureDTO.isActive()
        );

        structureRepository.save(structure);

        loggingService.logInfo(
            format(InfoDescription.CRETE_STRUCTURE_FORMAT, structure.getStructureName()
        ));

        return this.response;
    }

    private <T> T findById(long id, JpaRepository<T, Long> repository, String errorDescription) {
        Optional<T> optionalEntity = repository.findById(id);

        if (optionalEntity.isEmpty()) {
            this.response.addErrorDescription(errorDescription);
            setErrorResponse();
        }

        return optionalEntity.orElseThrow(EditEntityException::new);
    }

    private void setErrorResponse() {
        throw new EditEntityException(this.response.getErrorDescriptions().toString());
    }
}
