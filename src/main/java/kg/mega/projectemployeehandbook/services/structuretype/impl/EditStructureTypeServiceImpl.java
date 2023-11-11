package kg.mega.projectemployeehandbook.services.structuretype.impl;

import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.structuretype.EditStructureTypeDTO;
import kg.mega.projectemployeehandbook.models.entities.StructureType;
import kg.mega.projectemployeehandbook.repositories.StructureTypeRepository;
import kg.mega.projectemployeehandbook.services.ErrorCollectorService;
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
@FieldDefaults(level = PRIVATE)
public class EditStructureTypeServiceImpl implements EditStructureTypeService {
    final StructureTypeRepository structureTypeRepository;
    final ErrorCollectorService   errorCollectorService;
    final CommonRepositoryUtil    commonRepositoryUtil;
    final LoggingService          loggingService;

    @Override
    @Transactional
    public String editStructureType(EditStructureTypeDTO editStructureTypeDTO) {
        errorCollectorService.cleanup();

        StructureType structureType = commonRepositoryUtil.getEntityById(
            editStructureTypeDTO.getStructureTypeId(),
            structureTypeRepository,
            ErrorDescription.STRUCTURE_TYPE_ID_NOT_FOUND,
            ExceptionType.EDIT_ENTITY_EXCEPTION
        );

        if (!validateEditStructureType(editStructureTypeDTO, structureType)) {
            errorCollectorService.callException(ExceptionType.EDIT_ENTITY_EXCEPTION);
        }

        structureTypeRepository.save(structureType);
        String successfulResultMessage = format(InfoDescription.CREATE_STRUCTURE_TYPE_FORMAT, structureType.getStructureTypeName());
        loggingService.logInfo(successfulResultMessage);
        return successfulResultMessage;
    }

    private boolean validateEditStructureType(EditStructureTypeDTO editStructureTypeDTO, StructureType structureType) {
        boolean valid = true;
        valid &= checkAndSetNewStructureTypeName(editStructureTypeDTO.getNewStructureTypeName(), structureType);
        valid &= checkAndSetStructureTypeActive(editStructureTypeDTO.isDisable(), editStructureTypeDTO.isEnable(), structureType);
        return valid;
    }

    private boolean checkAndSetNewStructureTypeName(String newStructureTypeName, StructureType structureType) {
        if (newStructureTypeName.isBlank()) {
            return true;
        }
        if (newStructureTypeName.equals(structureType.getStructureTypeName())) {
            errorCollectorService.addErrorMessages(of(ErrorDescription.STRUCTURE_TYPE_NAME_ALREADY_USED));
            return false;
        }
        structureType.setStructureTypeName(newStructureTypeName);
        return true;
    }

    private boolean checkAndSetStructureTypeActive(boolean disable, boolean enable, StructureType structureType) {
        if (disable) {
            if (!structureType.getActive()) {
                errorCollectorService.addErrorMessages(of(ErrorDescription.STRUCTURE_TYPE_ALREADY_INACTIVE));
                return false;
            } else {
                structureType.setActive(false);
            }
        }
        if (enable) {
            if (structureType.getActive()) {
                errorCollectorService.addErrorMessages(of(ErrorDescription.STRUCTURE_TYPE_ALREADY_ACTIVE));
                return false;
            } else {
                structureType.setActive(true);
            }
        }
        return true;
    }
}
