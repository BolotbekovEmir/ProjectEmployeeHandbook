package kg.mega.projectemployeehandbook.services.structuretype.impl;

import kg.mega.projectemployeehandbook.errors.ErrorCollector;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.structuretype.EditStructureTypeDTO;
import kg.mega.projectemployeehandbook.models.entities.StructureType;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.repositories.StructureTypeRepository;
import kg.mega.projectemployeehandbook.services.log.InfoCollector;
import kg.mega.projectemployeehandbook.services.structuretype.EditStructureTypeService;
import kg.mega.projectemployeehandbook.utils.CommonRepositoryUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;
import static java.util.List.of;
import static lombok.AccessLevel.PRIVATE;

/**
 * Реализация сервиса для редактирования типов структур.
 * Позволяет изменять свойства типов структур на основе предоставленных данных.
 * Проверяет валидность изменений и записывает их в хранилище.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EditStructureTypeServiceImpl implements EditStructureTypeService {
    StructureTypeRepository structureTypeRepository;

    CommonRepositoryUtil commonRepositoryUtil;
    ErrorCollector       errorCollector;
    InfoCollector        infoCollector;

    /**
     * Выполняет редактирование типа структуры на основе предоставленных данных.
     * @param editStructureTypeDTO Данные для редактирования типа структуры.
     * @return Сообщение об успешном завершении операции редактирования типа структуры.
     * @throws kg.mega.projectemployeehandbook.errors.exceptions.EditEntityException Возникает при попытке редактирования типа структуры с недопустимыми значениями.
     */
    @Override
    @Transactional
    public String editStructureType(EditStructureTypeDTO editStructureTypeDTO) {
        errorCollector.cleanup();
        infoCollector.cleanup();

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
        infoCollector.setChangerInfo();
        infoCollector.setEntityInfo(structureType.getId(), structureType.getStructureTypeName());
        infoCollector.writeFullLog(operationSuccessMessage);

        return operationSuccessMessage;
    }

    /**
     * Проверяет валидность предложенных изменений для типа структуры.
     * Проверяет и устанавливает новое имя и активность для типа структуры.
     * В случае ошибки в любом из методов изменения, возвращает false.
     * @param editStructureTypeDTO Информация о редактируемом типе структуры.
     * @param structureType Тип структуры для редактирования.
     * @return true, если все предложенные изменения валидны, иначе - false.
     */
    private boolean validateEditStructureType(EditStructureTypeDTO editStructureTypeDTO, StructureType structureType) {
        boolean valid = true;
        valid &= checkAndSetNewStructureTypeName(editStructureTypeDTO.getNewStructureTypeName(), structureType);
        valid &= checkAndSetStructureTypeActive(editStructureTypeDTO.getDisable(), editStructureTypeDTO.getEnable(), structureType);
        return valid;
    }

    /**
     * Проверяет и устанавливает новое имя для типа структуры.
     * Если новое имя пустое или совпадает с текущим, добавляет сообщение об ошибке в коллектор ошибок.
     * Иначе, устанавливает новое имя для типа структуры и добавляет информацию об этом в коллектор информации.
     * @param newStructureTypeName Новое имя для типа структуры.
     * @param structureType Тип структуры, который редактируется.
     * @return true, если операция прошла успешно, иначе - false.
     */
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
        infoCollector.addFieldUpdatesInfo(
                "structureTypeName",
                structureType.getStructureTypeName(),
                newStructureTypeName
        );
        structureType.setStructureTypeName(newStructureTypeName);
        return true;
    }

    /**
     * Проверяет и устанавливает активность для типа структуры.
     * Если тип структуры уже имеет запрошенное состояние (включен или выключен), добавляет сообщение об ошибке.
     * Иначе, устанавливает запрошенное состояние для типа структуры и добавляет информацию об этом в коллектор информации.
     * @param disable Флаг для выключения типа структуры.
     * @param enable Флаг для включения типа структуры.
     * @param structureType Тип структуры, который редактируется.
     * @return true, если операция прошла успешно, иначе - false.
     */
    private boolean checkAndSetStructureTypeActive(boolean disable, boolean enable, StructureType structureType) {
        if (disable) {
            if (!structureType.getActive()) {
                errorCollector.addErrorMessages(
                    of(ErrorDescription.STRUCTURE_TYPE_ALREADY_INACTIVE)
                );
                return false;
            } else {
                infoCollector.addFieldUpdatesInfo("active", "true", "false");
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
                infoCollector.addFieldUpdatesInfo("active", "false", "true");
                structureType.setActive(true);
            }
        }
        return true;
    }
}
