package kg.mega.projectemployeehandbook.services.structure.impl;

import kg.mega.projectemployeehandbook.errors.EditEntityException;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.structure.EditStructureDTO;
import kg.mega.projectemployeehandbook.models.entities.Structure;
import kg.mega.projectemployeehandbook.models.entities.StructureType;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;
import kg.mega.projectemployeehandbook.repositories.StructureRepository;
import kg.mega.projectemployeehandbook.repositories.StructureTypeRepository;
import kg.mega.projectemployeehandbook.services.log.LoggingService;
import kg.mega.projectemployeehandbook.services.structure.EditStructureService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import static java.lang.String.*;
import static lombok.AccessLevel.*;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class EditStructureServiceImpl implements EditStructureService {
    final StructureTypeRepository structureTypeRepository;
    final StructureRepository     structureRepository;
    final LoggingService          loggingService;

    final RestResponse<EditEntityException> response = new RestResponse<>();

    @Override
    public RestResponse<EditEntityException> editStructure(EditStructureDTO editStructureDTO) {
        long
            structureId         = editStructureDTO.getStructureId(),
            newMasterId         = editStructureDTO.getNewMasterId(),
            newStructureTypeId  = editStructureDTO.getNewStructureTypeId();

        String newStructureName = editStructureDTO.getNewStructureName();

        boolean
            disable = editStructureDTO.isDisable(),
            enable  = editStructureDTO.isEnable();

        Structure structure = findById(structureRepository, structureId, ErrorDescription.STRUCTURE_ID_NOT_FOUND);

        if (newMasterId != 0) {
            Structure structureMaster = findById(structureRepository, newMasterId, ErrorDescription.STRUCTURE_ID_NOT_FOUND);
            structure.setMaster(structureMaster);
        }

        if (newStructureTypeId != 0) {
            StructureType structureType = findById(
                structureTypeRepository, newStructureTypeId, ErrorDescription.STRUCTURE_TYPE_ID_NOT_FOUND
            );
            structure.setStructureType(structureType);
        }

        updateStructureName(structure, newStructureName);
        disableStructure(structure, disable);
        enableStructure(structure, enable);

        structureRepository.save(structure);

        loggingService.logInfo(
            format(InfoDescription.EDIT_STRUCTURE_FORMAT, structure.getId())
        );

        this.response.setHttpResponse(OK, OK.value());

        return this.response;
    }

    private <T> T findById(JpaRepository<T, Long> repository, long id, String errorMessage) {
        return repository.findById(id).orElseThrow(
            () -> new EditEntityException(errorMessage)
        );
    }

    private void updateStructureName(Structure structure, String structureName) {
        if (!structureName.isBlank()) {
            if (structure.getStructureName().equals(structureName)) {
                throw new EditEntityException(ErrorDescription.STRUCTURE_NAME_ALREADY_HAVE_THIS_NAME);
            } else {
                structure.setStructureName(structureName);
            }
        }
    }

    private void disableStructure(Structure structure, boolean disable) {
        if (disable) {
            if (!structure.getActive()) {
                throw new EditEntityException(ErrorDescription.STRUCTURE_ALREADY_INACTIVE);
            } else {
                structure.setActive(false);
            }
        }
    }

    private void enableStructure(Structure structure, boolean enable) {
        if (enable) {
            if (structure.getActive()) {
                throw new EditEntityException(ErrorDescription.STRUCTURE_ALREADY_ACTIVE);
            } else {
                structure.setActive(true);
            }
        }
    }

}
