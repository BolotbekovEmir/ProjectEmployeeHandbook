package kg.mega.projectemployeehandbook.services.structure.impl;

import kg.mega.projectemployeehandbook.errors.ErrorCollector;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.structure.EditStructureDTO;
import kg.mega.projectemployeehandbook.models.entities.Structure;
import kg.mega.projectemployeehandbook.models.entities.StructureType;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.repositories.StructureRepository;
import kg.mega.projectemployeehandbook.repositories.StructureTypeRepository;
import kg.mega.projectemployeehandbook.services.log.InfoCollector;
import kg.mega.projectemployeehandbook.services.structure.EditStructureService;
import kg.mega.projectemployeehandbook.utils.CommonRepositoryUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static java.lang.String.format;
import static java.util.List.of;
import static lombok.AccessLevel.PRIVATE;

/**
 * Сервис для изменения структур.
 * */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EditStructureServiceImpl implements EditStructureService {
    StructureTypeRepository structureTypeRepository;
    StructureRepository     structureRepository;

    CommonRepositoryUtil commonRepositoryUtil;
    ErrorCollector       errorCollector;
    InfoCollector        infoCollector;

    /**
     * Изменяет структуру на основе переданных данных.
     * @param editStructureDTO Данные для изменения структуры.
     * @return Сообщение об успешном выполнении операции изменения структуры.
     */
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

    /**
     * Проверяет данные для изменения структуры на корректность.
     * @param editStructureDTO Данные для изменения структуры.
     * @param structure Текущая структура, которую требуется изменить.
     * @return true, если данные корректны; в противном случае - false.
     */
    private boolean validateEditStructure(EditStructureDTO editStructureDTO, Structure structure) {
        boolean valid = true;
        valid &= checkAndSetNewMaster(editStructureDTO.getNewMasterId(), structure);
        valid &= checkAndSetNewStructureType(editStructureDTO.getNewStructureTypeId(), structure);
        valid &= checkAndSetNewStructureName(editStructureDTO.getNewStructureName(), structure);
        valid &= checkAndSetNewStructureActive(editStructureDTO.getDisable(), editStructureDTO.getEnable(), structure);
        return valid;
    }

    /**
     * Проверяет и устанавливает нового руководителя структуры.
     * @param newMasterId ID нового руководителя структуры.
     * @param structure Текущая структура, которую требуется изменить.
     * @return true, если руководитель установлен успешно или не требуется изменение; в противном случае - false.
     */
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

    /**
     * Проверяет и устанавливает новый тип структуры.
     * @param newStructureTypeId ID нового типа структуры.
     * @param structure Текущая структура, которую требуется изменить.
     * @return true, если тип структуры установлен успешно или не требуется изменение; в противном случае - false.
     */
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

    /**
     * Проверяет и устанавливает новое имя структуры.
     * @param newStructureName Новое имя структуры.
     * @param structure Текущая структура, которую требуется изменить.
     * @return true, если имя структуры установлено успешно или не требуется изменение; в противном случае - false.
     */
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

    /**
     * Проверяет и устанавливает новый статус активности структуры.
     * @param disable Флаг отключения структуры.
     * @param enable Флаг включения структуры.
     * @param structure Текущая структура, которую требуется изменить.
     * @return true, если статус активности установлен успешно или не требуется изменение; в противном случае - false.
     */
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
