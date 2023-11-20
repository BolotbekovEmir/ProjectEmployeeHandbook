package kg.mega.projectemployeehandbook.services.structure.impl;

import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.structure.EditStructureDTO;
import kg.mega.projectemployeehandbook.models.entities.Structure;
import kg.mega.projectemployeehandbook.models.entities.StructureType;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.repositories.StructureRepository;
import kg.mega.projectemployeehandbook.repositories.StructureTypeRepository;
import kg.mega.projectemployeehandbook.errors.ErrorCollector;
import kg.mega.projectemployeehandbook.services.log.InfoCollector;
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
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EditStructureServiceImpl implements EditStructureService {
    StructureTypeRepository structureTypeRepository;
    StructureRepository     structureRepository;

    LoggingService loggingService;

    CommonRepositoryUtil commonRepositoryUtil;
    ErrorCollector       errorCollector;
    InfoCollector        infoCollector;

    @Override
    @Transactional
    public String editStructure(EditStructureDTO editStructureDTO) {
        errorCollector.cleanup();
        infoCollector.cleanup();

        Structure structure = commonRepositoryUtil.getEntityById(
            editStructureDTO.getStructureId(),
            structureRepository,
            ErrorDescription.STRUCTURE_ID_NOT_FOUND
        );

        if (!validateEditStructure(editStructureDTO, structure)) {
            errorCollector.callException(ExceptionType.EDIT_ENTITY_EXCEPTION);
        }

        structureRepository.save(structure);

        String operationSuccessMessage = format(InfoDescription.EDIT_STRUCTURE_FORMAT, structure.getId());

        infoCollector.setChangerInfo();
        infoCollector.setEntityInfo(structure.getId(), structure.getStructureName());
        infoCollector.writeFullLog(operationSuccessMessage);

        return operationSuccessMessage;
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
            ErrorDescription.STRUCTURE_ID_NOT_FOUND
        );

        if (Objects.equals(structure.getMaster(), masterStructure)) {
            errorCollector.addErrorMessages(
                of(ErrorDescription.STRUCTURE_ALREADY_THIS_MASTER)
            );
            return false;
        } else {
            infoCollector.addFieldUpdatesInfo(
                    "master",
                    structure.getMaster().getId().toString(),
                    newMasterId.toString()
            );
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
            ErrorDescription.STRUCTURE_TYPE_ID_NOT_FOUND
        );

        if (Objects.equals(structure.getStructureType(), structureType)) {
            errorCollector.addErrorMessages(
                of(ErrorDescription.STRUCTURE_ALREADY_THIS_STRUCTURE_TYPE)
            );
            return false;
        } else {
            infoCollector.addFieldUpdatesInfo(
                    "structureType",
                    structure.getStructureType().getId().toString(),
                    newStructureTypeId.toString()
            );
            structure.setStructureType(structureType);
            return true;
        }
    }

    private boolean checkAndSetNewStructureName(String newStructureName, Structure structure) {
        if (newStructureName.isBlank()) {
            return true;
        }

        if (newStructureName.equals(structure.getStructureName())) {
            errorCollector.addErrorMessages(
                of(ErrorDescription.STRUCTURE_NAME_ALREADY_HAVE_THIS_NAME)
            );
            return false;
        } else {
            infoCollector.addFieldUpdatesInfo(
                    "structureName",
                    structure.getStructureName(),
                    newStructureName
            );
            structure.setStructureName(newStructureName);
            return true;
        }
    }

    private boolean checkAndSetNewStructureActive(boolean disable, boolean enable, Structure structure) {
        if (disable) {
            if (!structure.getActive()) {
                errorCollector.addErrorMessages(
                    of(ErrorDescription.STRUCTURE_ALREADY_INACTIVE)
                );
                return false;
            } else {
                infoCollector.addFieldUpdatesInfo("active", "true", "false");
                structure.setActive(false);
                return true;
            }
        }
        if (enable) {
            if (structure.getActive()) {
                errorCollector.addErrorMessages(
                    of(ErrorDescription.STRUCTURE_ALREADY_ACTIVE)
                );
                return false;
            } else {
                infoCollector.addFieldUpdatesInfo("active", "false", "true");
                structure.setActive(true);
                return true;
            }
        }
        return true;
    }
}
