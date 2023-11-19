package kg.mega.projectemployeehandbook.services.structuretype.impl;

import kg.mega.projectemployeehandbook.configuration.MapperConfiguration;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.structuretype.CreateStructureTypeDTO;
import kg.mega.projectemployeehandbook.models.entities.StructureType;
import kg.mega.projectemployeehandbook.repositories.StructureTypeRepository;
import kg.mega.projectemployeehandbook.errors.ErrorCollector;
import kg.mega.projectemployeehandbook.services.log.LoggingService;
import kg.mega.projectemployeehandbook.services.structuretype.CreateStructureTypeService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static java.util.List.*;
import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CreateStructureTypeServiceImpl implements CreateStructureTypeService {
    StructureTypeRepository structureTypeRepository;

    LoggingService loggingService;

    MapperConfiguration mapper;
    ErrorCollector      errorCollector;

    @Override
    public String createStructureType(CreateStructureTypeDTO createStructureTypeDTO) {
        errorCollector.cleanup();

        if (!validateStructureTypeName(createStructureTypeDTO.getStructureTypeName())) {
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
        loggingService.logInfo(operationSuccessMessage);
        return operationSuccessMessage;
    }

    private boolean validateStructureTypeName(String structureTypeName) {
        return !structureTypeName.isBlank();
    }

}