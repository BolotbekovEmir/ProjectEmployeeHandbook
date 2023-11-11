package kg.mega.projectemployeehandbook.services.structure.impl;

import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.structure.EditStructureDTO;
import kg.mega.projectemployeehandbook.models.entities.Structure;
import kg.mega.projectemployeehandbook.models.entities.StructureType;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.repositories.StructureRepository;
import kg.mega.projectemployeehandbook.repositories.StructureTypeRepository;
import kg.mega.projectemployeehandbook.services.ErrorCollectorService;
import kg.mega.projectemployeehandbook.services.log.LoggingService;
import kg.mega.projectemployeehandbook.services.structure.EditStructureService;
import kg.mega.projectemployeehandbook.utils.CommonRepositoryUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static java.lang.String.format;
import static java.util.List.*;
import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class EditStructureServiceImpl implements EditStructureService {
    final StructureTypeRepository structureTypeRepository;
    final ErrorCollectorService   errorCollectorService;
    final CommonRepositoryUtil    commonRepositoryUtil;
    final StructureRepository     structureRepository;
    final LoggingService          loggingService;

    @Override
    @Transactional
    public String editStructure(EditStructureDTO editStructureDTO) {
        errorCollectorService.cleanup();

        Structure structure = commonRepositoryUtil.getEntityById(
            editStructureDTO.getStructureId(),
            structureRepository,
            ErrorDescription.STRUCTURE_ID_NOT_FOUND,
            ExceptionType.EDIT_ENTITY_EXCEPTION
        );

        if (!validateEditStructure(editStructureDTO, structure)) {
            errorCollectorService.callException(ExceptionType.EDIT_ENTITY_EXCEPTION);
        }

        String successfulResultMessage = format(InfoDescription.EDIT_STRUCTURE_FORMAT, 0);
        loggingService.logInfo(successfulResultMessage);
        return successfulResultMessage;
    }

    private boolean validateEditStructure(EditStructureDTO editStructureDTO, Structure structure) {
        boolean valid = true;
        valid &= checkAndSetNewMaster(editStructureDTO.getNewMasterId(), structure);
        valid &= checkAndSetNewStructureType(editStructureDTO.getNewStructureTypeId(), structure);
        valid &= checkAndSetNewStructureName(editStructureDTO.getNewStructureName(), structure);
        valid &= checkAndSetNewStructureActive(editStructureDTO.isDisable(), editStructureDTO.isEnable(), structure);
        return valid;
    }

    private boolean checkAndSetNewMaster(Long newMasterId, Structure structure) {
        if (newMasterId == null) {
            return true;
        }

        Structure masterStructure = commonRepositoryUtil.getEntityById(
            newMasterId,
            structureRepository,
            ErrorDescription.STRUCTURE_ID_NOT_FOUND,
            ExceptionType.EDIT_ENTITY_EXCEPTION
        );

        if (Objects.equals(structure.getMaster(), masterStructure)) {
            errorCollectorService.addErrorMessages(
                of(ErrorDescription.STRUCTURE_ALREADY_THIS_MASTER)
            );
            return false;
        } else {
            structure.setMaster(masterStructure);
            return true;
        }
    }

    private boolean checkAndSetNewStructureType(Long newStructureTypeId, Structure structure) {
        if (newStructureTypeId == null) {
            return true;
        }

        StructureType structureType = commonRepositoryUtil.getEntityById(
            newStructureTypeId,
            structureTypeRepository,
            ErrorDescription.STRUCTURE_TYPE_ID_NOT_FOUND,
            ExceptionType.EDIT_ENTITY_EXCEPTION
        );

        if (Objects.equals(structure.getStructureType(), structureType)) {
            errorCollectorService.addErrorMessages(
                of(ErrorDescription.STRUCTURE_ALREADY_THIS_STRUCTURE_TYPE)
            );
            return false;
        } else {
            structure.setStructureType(structureType);
            return true;
        }
    }

    private boolean checkAndSetNewStructureName(String newStructureName, Structure structure) {
        if (newStructureName.isBlank()) {
            return true;
        }

        if (newStructureName.equals(structure.getStructureName())) {
            errorCollectorService.addErrorMessages(
                of(ErrorDescription.STRUCTURE_NAME_ALREADY_HAVE_THIS_NAME)
            );
            return false;
        } else {
            structure.setStructureName(newStructureName);
            return true;
        }
    }

    private boolean checkAndSetNewStructureActive(boolean disable, boolean enable, Structure structure) {
        if (disable) {
            if (!structure.getActive()) {
                errorCollectorService.addErrorMessages(
                    of(ErrorDescription.STRUCTURE_ALREADY_INACTIVE)
                );
                return false;
            } else {
                structure.setActive(false);
                return true;
            }
        }
        if (enable) {
            if (structure.getActive()) {
                errorCollectorService.addErrorMessages(
                    of(ErrorDescription.STRUCTURE_ALREADY_ACTIVE)
                );
                return false;
            } else {
                structure.setActive(true);
                return true;
            }
        }
        return true;
    }
}
