package kg.mega.projectemployeehandbook.services.structuretype.impl;

import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.structuretype.EditStructureTypeDTO;
import kg.mega.projectemployeehandbook.models.entities.StructureType;
import kg.mega.projectemployeehandbook.repositories.StructureTypeRepository;
import kg.mega.projectemployeehandbook.errors.ErrorCollector;
import kg.mega.projectemployeehandbook.services.log.LoggingService;
import kg.mega.projectemployeehandbook.services.structuretype.EditStructureTypeService;
import kg.mega.projectemployeehandbook.utils.CommonRepositoryUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;
import static java.util.List.of;
import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EditStructureTypeServiceImpl implements EditStructureTypeService {
    StructureTypeRepository structureTypeRepository;

    LoggingService loggingService;

    CommonRepositoryUtil commonRepositoryUtil;
    ErrorCollector       errorCollector;


    @Override
    @Transactional
    public String editStructureType(EditStructureTypeDTO editStructureTypeDTO) {
        errorCollector.cleanup();

        StructureType structureType = commonRepositoryUtil.getEntityById(
            editStructureTypeDTO.getStructureTypeId(),
            structureTypeRepository,
            ErrorDescription.STRUCTURE_TYPE_ID_NOT_FOUND
        );

        if (!validateEditStructureType(editStructureTypeDTO, structureType)) {
            errorCollector.callException(ExceptionType.EDIT_ENTITY_EXCEPTION);
        }

        structureTypeRepository.save(structureType);

        String operationSuccessMessage = format(InfoDescription.EDIT_STRUCTURE_TYPE_FORMAT, structureType.getId());
        loggingService.logInfo(operationSuccessMessage);
        return operationSuccessMessage;
    }

    private boolean validateEditStructureType(EditStructureTypeDTO editStructureTypeDTO, StructureType structureType) {
        boolean valid = true;
        valid &= checkAndSetNewStructureTypeName(editStructureTypeDTO.getNewStructureTypeName(), structureType);
        valid &= checkAndSetStructureTypeActive(editStructureTypeDTO.getDisable(), editStructureTypeDTO.getEnable(), structureType);
        return valid;
    }

    private boolean checkAndSetNewStructureTypeName(String newStructureTypeName, StructureType structureType) {
        if (newStructureTypeName.isBlank()) {
            return true;
        }
        if (newStructureTypeName.equals(structureType.getStructureTypeName())) {
            errorCollector.addErrorMessages(
                of(ErrorDescription.STRUCTURE_TYPE_NAME_ALREADY_USED)
            );
            return false;
        }
        structureType.setStructureTypeName(newStructureTypeName);
        return true;
    }

    private boolean checkAndSetStructureTypeActive(boolean disable, boolean enable, StructureType structureType) {
        if (disable) {
            if (!structureType.getActive()) {
                errorCollector.addErrorMessages(
                    of(ErrorDescription.STRUCTURE_TYPE_ALREADY_INACTIVE)
                );
                return false;
            } else {
                structureType.setActive(false);
            }
        }
        if (enable) {
            if (structureType.getActive()) {
                errorCollector.addErrorMessages(
                    of(ErrorDescription.STRUCTURE_TYPE_ALREADY_ACTIVE)
                );
                return false;
            } else {
                structureType.setActive(true);
            }
        }
        return true;
    }
}
