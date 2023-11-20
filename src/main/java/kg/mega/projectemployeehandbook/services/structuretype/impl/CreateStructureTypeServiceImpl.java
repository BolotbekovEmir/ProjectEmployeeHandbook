package kg.mega.projectemployeehandbook.services.structuretype.impl;

import kg.mega.projectemployeehandbook.configuration.MapperConfiguration;
import kg.mega.projectemployeehandbook.errors.ErrorCollector;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.structuretype.CreateStructureTypeDTO;
import kg.mega.projectemployeehandbook.models.entities.StructureType;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.repositories.StructureTypeRepository;
import kg.mega.projectemployeehandbook.services.log.InfoCollector;
import kg.mega.projectemployeehandbook.services.structuretype.CreateStructureTypeService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static java.util.List.of;
import static lombok.AccessLevel.PRIVATE;

/**
 * Реализация сервиса для создания типов структур.
 * Принимает данные и создает новый тип структуры, используя переданные параметры.
 * Выполняет запись нового типа структуры в репозиторий.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CreateStructureTypeServiceImpl implements CreateStructureTypeService {
    StructureTypeRepository structureTypeRepository;

    MapperConfiguration mapper;
    ErrorCollector      errorCollector;
    InfoCollector       infoCollector;

    /**
     * Создает новый тип структуры на основе переданных данных.
     * @param createStructureTypeDTO Данные для создания типа структуры.
     * @return Сообщение об успешном завершении операции создания типа структуры.
     * @throws kg.mega.projectemployeehandbook.errors.exceptions.CreateEntityException Возникает при попытке создания типа структуры с пустым именем.
     */
    @Override
    public String createStructureType(CreateStructureTypeDTO createStructureTypeDTO) {
        errorCollector.cleanup();
        infoCollector.cleanup();

        if (createStructureTypeDTO.getStructureTypeName().isBlank()) {
            errorCollector.addErrorMessages(
                of(ErrorDescription.STRUCTURE_TYPE_NAME_IS_EMPTY)
            );
            errorCollector.callException(ExceptionType.CREATE_ENTITY_EXCEPTION);
        }

        StructureType structureType = mapper.getMapper().map(createStructureTypeDTO, StructureType.class);

        structureTypeRepository.save(structureType);

        String operationSuccessMessage = format(
            InfoDescription.CREATE_STRUCTURE_TYPE_FORMAT, structureType.getId()
        );

        infoCollector.setChangerInfo();
        infoCollector.setEntityInfo(structureType.getId(), structureType.getStructureTypeName());
        infoCollector.writeEntityLog(operationSuccessMessage);

        return operationSuccessMessage;
    }
}