package kg.mega.projectemployeehandbook.services.structure.impl;

import kg.mega.projectemployeehandbook.errors.ErrorCollector;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.structure.CreateStructureDTO;
import kg.mega.projectemployeehandbook.models.entities.Structure;
import kg.mega.projectemployeehandbook.models.entities.StructureType;
import kg.mega.projectemployeehandbook.repositories.StructureRepository;
import kg.mega.projectemployeehandbook.repositories.StructureTypeRepository;
import kg.mega.projectemployeehandbook.services.log.InfoCollector;
import kg.mega.projectemployeehandbook.services.structure.CreateStructureService;
import kg.mega.projectemployeehandbook.utils.CommonRepositoryUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CreateStructureServiceImpl implements CreateStructureService {
    StructureTypeRepository structureTypeRepository;
    StructureRepository     structureRepository;

    CommonRepositoryUtil commonRepositoryUtil;
    ErrorCollector       errorCollector;
    InfoCollector        infoCollector;

    @Override
    public String createStructure(CreateStructureDTO createStructureDTO) {
        errorCollector.cleanup();
        infoCollector.cleanup();

        Structure structureMaster = commonRepositoryUtil.getEntityById(
            createStructureDTO.getMasterId(),
            structureRepository,
            ErrorDescription.STRUCTURE_ID_NOT_FOUND
        );

        StructureType structureType = commonRepositoryUtil.getEntityById(
            createStructureDTO.getStructureTypeId(),
            structureTypeRepository,
            ErrorDescription.STRUCTURE_TYPE_ID_NOT_FOUND
        );

        Structure structure = new Structure(
            structureMaster,
            structureType,
            createStructureDTO.getStructureName(),
            createStructureDTO.isActive()
        );

        structureRepository.save(structure);

        String operationSuccessMessage = format(InfoDescription.CRETE_STRUCTURE_FORMAT, structure.getId());
        infoCollector.setChangerInfo();
        infoCollector.setEntityInfo(structure.getId(), structure.getStructureName());
        infoCollector.writeEntityLog(operationSuccessMessage);

        return operationSuccessMessage;
    }
}
