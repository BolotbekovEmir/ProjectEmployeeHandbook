package kg.mega.projectemployeehandbook.services.structure.impl;

import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.models.entities.Structure;
import kg.mega.projectemployeehandbook.models.entities.junction.EmployeeStructure;
import kg.mega.projectemployeehandbook.repositories.EmployeeRepository;
import kg.mega.projectemployeehandbook.repositories.StructureRepository;
import kg.mega.projectemployeehandbook.repositories.junction.EmployeeStructureRepository;
import kg.mega.projectemployeehandbook.services.structure.SearchStructureService;
import kg.mega.projectemployeehandbook.utils.CommonRepositoryUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

/**
 * Сервис для поиска структур.
 * */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SearchStructureServiceImpl implements SearchStructureService {
    EmployeeStructureRepository employeeStructureRepository;
    StructureRepository         structureRepository;
    EmployeeRepository          employeeRepository;

    CommonRepositoryUtil commonRepositoryUtil;

    /**
     * Поиск структуры по заданному полю.
     * @param searchField Поле для поиска структуры.
     * @return Множество строк, представляющих найденные структуры.
     */
    @Override
    public Set<String> searchStructure(String searchField) {
        Set<Structure> structures = structureRepository.findAllByStructureNameContainsIgnoreCaseAndActiveIsTrue(searchField);

        try {
            Long structureId = Long.parseLong(searchField);
            Structure structure = commonRepositoryUtil.getEntityById(
                structureId,
                structureRepository,
                ErrorDescription.STRUCTURE_ID_NOT_FOUND
            );
            structures.add(structure);
        } catch (NumberFormatException exception) {
            /* Исключение игнорируется, так как searchField не содержит id */
        }

        return structureParses(structures);
    }

    /**
     * Поиск структур сотрудника по его ID.
     * @param employeeId ID сотрудника для поиска структур.
     * @return Множество строк, представляющих структуры сотрудника.
     */
    @Override
    public Set<String> searchEmployeeStructures(Long employeeId) {
        Set<Structure> structures = employeeStructureRepository.findAllByEmployeeAndEndDateIsNull(
            commonRepositoryUtil.getEntityById(
                employeeId,
                employeeRepository,
                ErrorDescription.EMPLOYEE_ID_NOT_FOUND
            )
        ).stream()
            .map(EmployeeStructure::getStructure)
            .collect(Collectors.toSet());

        return structureParses(structures);
    }

    /**
     * Преобразует структуры в строковое представление для вывода.
     * @param structures Множество структур для преобразования.
     * @return Множество строковых представлений структур.
     */
    private Set<String> structureParses(Set<Structure> structures) {
        return structures.stream()
            .map(structure -> {
                StringBuilder resultParse = new StringBuilder();
                if (structure.getMaster() != null) {
                    resultParse.append(structure.getStructureName());
                    do {
                        resultParse
                            .append(" / ")
                            .append(structure.getMaster().getStructureName());
                        structure = structure.getMaster();
                    } while (structure.getMaster() != null);
                } else {
                    resultParse.append(structure.getStructureName());
                }
                resultParse.append(" (CEO)");
                return resultParse.toString();
            }).collect(Collectors.toSet());
    }
}