package kg.mega.projectemployeehandbook.services.structuretype.impl;

import kg.mega.projectemployeehandbook.configuration.MapperConfiguration;
import kg.mega.projectemployeehandbook.errors.CreateEntityException;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.structuretype.CreateStructureTypeDTO;
import kg.mega.projectemployeehandbook.models.entities.StructureType;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;
import kg.mega.projectemployeehandbook.repositories.StructureTypeRepository;
import kg.mega.projectemployeehandbook.services.log.LoggingService;
import kg.mega.projectemployeehandbook.services.structuretype.CreateStructureTypeService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static java.lang.String.*;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.CREATED;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class CreateStructureTypeServiceImpl implements CreateStructureTypeService {
    final StructureTypeRepository structureTypeRepository;
    final LoggingService          loggingService;
    final MapperConfiguration     mapper;

    final RestResponse<CreateEntityException> response = new RestResponse<>();

    @Override
    public RestResponse<CreateEntityException> createStructureType(CreateStructureTypeDTO createStructureTypeDTO) {
        this.response.setErrorDescriptions(new ArrayList<>());

        StructureType structureType = mapper.getMapper().map(createStructureTypeDTO, StructureType.class);

        structureTypeRepository.save(structureType);

        this.response.setHttpResponse(CREATED, CREATED.value());

        loggingService.logInfo(
            format(InfoDescription.CREATE_STRUCTURE_TYPE_FORMAT, createStructureTypeDTO.getStructureTypeName())
        );

        return response;
    }

}