package kg.mega.projectemployeehandbook.services.structuretype.impl;

import kg.mega.projectemployeehandbook.errors.EditEntityException;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.structuretype.EditStructureTypeDTO;
import kg.mega.projectemployeehandbook.models.entities.StructureType;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;
import kg.mega.projectemployeehandbook.repositories.StructureTypeRepository;
import kg.mega.projectemployeehandbook.services.log.LoggingService;
import kg.mega.projectemployeehandbook.services.structuretype.EditStructureTypeService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

import static java.lang.String.*;
import static lombok.AccessLevel.*;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class EditStructureTypeServiceImpl implements EditStructureTypeService {
    final StructureTypeRepository structureTypeRepository;
    final LoggingService          loggingService;

    final RestResponse<EditEntityException> response = new RestResponse<>();

    @Override
    public RestResponse<EditEntityException> editStructureType(EditStructureTypeDTO editStructureTypeDTO) {
        this.response.setErrorDescriptions(new ArrayList<>());

        StructureType structureType = getStructureTypeFindById(editStructureTypeDTO.getStructureTypeId());

        updateStructureTypeName(structureType, editStructureTypeDTO.getNewStructureTypeName());
        disableStructureType(structureType, editStructureTypeDTO.isDisable());
        enableStructureType(structureType, editStructureTypeDTO.isEnable());

        structureTypeRepository.save(structureType);

        this.response.setHttpResponse(OK, OK.value());
        loggingService.logInfo(
            format(InfoDescription.EDIT_STRUCTURE_TYPE_FORMAT, structureType.getId())
        );

        return this.response;
    }

    private void updateStructureTypeName(StructureType structureType, String newStructureTypeName) {
        if (!newStructureTypeName.isBlank()) {
            structureType.setStructureTypeName(newStructureTypeName);
        }
    }

    private void disableStructureType(StructureType structureType, boolean disable) {
        if (disable) {
            if (structureType.getActive()) {
                structureType.setActive(false);
            } else {
                this.response.addErrorDescription(ErrorDescription.STRUCTURE_TYPE_ALREADY_INACTIVE);
                setErrorResponse();
            }
        }
    }

    private void enableStructureType(StructureType structureType, boolean enable) {
        if (enable) {
            if (!structureType.getActive()) {
                structureType.setActive(true);
            } else {
                this.response.addErrorDescription(ErrorDescription.STRUCTURE_TYPE_ALREADY_ACTIVE);
                setErrorResponse();
            }
        }
    }

    private StructureType getStructureTypeFindById(long structureTypeId) {
        Optional<StructureType> optionalStructureType = structureTypeRepository.findById(structureTypeId);

        if (optionalStructureType.isEmpty()) {
            this.response.addErrorDescription(ErrorDescription.STRUCTURE_TYPE_ID_NOT_FOUND);
            setErrorResponse();
        }

        return optionalStructureType.orElseThrow(EditEntityException::new);
    }

    private void setErrorResponse() {
        throw new EditEntityException(this.response.getErrorDescriptions().toString());
    }

}
