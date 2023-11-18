package kg.mega.projectemployeehandbook.services.employee.impl;

import kg.mega.projectemployeehandbook.models.dto.employee.GetEmployeeDTO;
import kg.mega.projectemployeehandbook.models.dto.position.GetPositionDTO;
import kg.mega.projectemployeehandbook.models.entities.Employee;
import kg.mega.projectemployeehandbook.models.entities.junction.EmployeePosition;
import kg.mega.projectemployeehandbook.models.entities.junction.EmployeeStructure;
import kg.mega.projectemployeehandbook.models.enums.Status;
import kg.mega.projectemployeehandbook.repositories.EmployeeRepository;
import kg.mega.projectemployeehandbook.repositories.PositionRepository;
import kg.mega.projectemployeehandbook.repositories.StructureRepository;
import kg.mega.projectemployeehandbook.repositories.junction.EmployeePositionRepository;
import kg.mega.projectemployeehandbook.repositories.junction.EmployeeStructureRepository;
import kg.mega.projectemployeehandbook.services.employee.SearchEmployeeService;
import kg.mega.projectemployeehandbook.services.structure.SearchStructureService;
import kg.mega.projectemployeehandbook.utils.CommonRepositoryUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SearchEmployeeServiceImpl implements SearchEmployeeService {
    EmployeeStructureRepository employeeStructureRepository;
    EmployeePositionRepository  employeePositionRepository;
    StructureRepository         structureRepository;
    EmployeeRepository          employeeRepository;
    PositionRepository          positionRepository;

    SearchStructureService searchStructureService;

    CommonRepositoryUtil commonRepositoryUtil;

    @Override
    public Set<GetEmployeeDTO> searchEmployees(String searchField) {
        Set<Employee>
            employeesByFirstname      = employeeRepository.findAllByFirstnameContainsIgnoreCase     (searchField),
            employeesByLastname       = employeeRepository.findAllByLastnameContainsIgnoreCase      (searchField),
            employeesByPatronimyc     = employeeRepository.findAllByPatronimycContainsIgnoreCase    (searchField),
            employeesByPersonalNumber = employeeRepository.findAllByPersonalNumber                  (searchField),
            employeesByPhone          = employeeRepository.findAllByPhone                           (searchField),
            employeesByEmail          = employeeRepository.findAllByEmail                           (searchField),
            employeesByPostalAddress  = employeeRepository.findAllByPostalAddressContainsIgnoreCase (searchField);


        Set<Employee> employeesByStatus = employeeRepository.findAllByStatus(
            commonRepositoryUtil.getEnumByStringName(Status.class, searchField)
        );

        Set<Employee> employeesByStructure = employeeStructureRepository.findAllByStructure(
            structureRepository.findAllByStructureNameContainsIgnoreCaseAndActiveIsTrue(searchField)
                .stream()
                .findAny()
                .orElse(null)
            ).stream()
            .filter(employeeStructure -> employeeStructure.getEndDate() == null || employeeStructure.getEndDate().toString().isBlank())
            .map(EmployeeStructure::getEmployee)
            .collect(Collectors.toSet());

        Set<Employee> employeesByPosition = employeePositionRepository.findAllByPosition(
            positionRepository.findAllByPositionNameContainsIgnoreCaseAndActiveIsTrue(searchField)
                .stream()
                .findAny()
                .orElse(null)
            ).stream()
            .filter(employeePosition -> employeePosition.getEndDate() == null || employeePosition.getEndDate().toString().isBlank())
            .map(EmployeePosition::getEmployee)
            .collect(Collectors.toSet());

        Set<Employee> resultEmployeesSearch = new HashSet<>();

        resultEmployeesSearch.addAll(employeesByFirstname);
        resultEmployeesSearch.addAll(employeesByLastname);
        resultEmployeesSearch.addAll(employeesByPatronimyc);
        resultEmployeesSearch.addAll(employeesByPersonalNumber);
        resultEmployeesSearch.addAll(employeesByPhone);
        resultEmployeesSearch.addAll(employeesByEmail);
        resultEmployeesSearch.addAll(employeesByPostalAddress);

        resultEmployeesSearch.addAll(employeesByStatus);
        resultEmployeesSearch.addAll(employeesByStructure);
        resultEmployeesSearch.addAll(employeesByPosition);

        return mapper(resultEmployeesSearch);
    }

    private Set<GetEmployeeDTO> mapper(Set<Employee> employees) {
        return employees.stream()
            .map(employee -> new GetEmployeeDTO(
                employee.getLastname()
                    .concat(" ")
                    .concat(employee.getFirstname())
                    .concat(" ")
                    .concat(employee.getPatronimyc()),
                employee.getPersonalNumber(),
                employee.getPhone(),
                employee.getEmail(),
                employee.getPostalAddress(),
                employee.getPathPhoto(),
                employee.getFamilyStatus(),
                employee.getStatus(),
                employeePositionRepository.findAllByEmployee(employee).stream()
                    .map(employeePosition ->
                        employeePosition.getPosition().getMaster() == null
                            ? new GetPositionDTO(
                            employeePosition.getPosition().getId(),
                            null,
                            employeePosition.getPosition().getPositionName(),
                            employeePosition.getPosition().getActive())
                            : new GetPositionDTO(
                            employeePosition.getPosition().getId(),
                            employeePosition.getPosition().getMaster().getId(),
                            employeePosition.getPosition().getPositionName(),
                            employeePosition.getPosition().getActive())
                    ).collect(Collectors.toSet()),
                searchStructureService.searchStructure(
                    employeeStructureRepository.findAllByEmployee(employee).stream()
                        .map(employeeStructure -> employeeStructure.getStructure()
                            .getId()
                            .toString())
                        .findAny()
                        .orElse(null)),
                employee.getEmploymentDate(),
                employee.getBirthDate()
            ))
            .collect(Collectors.toSet());
    }
}