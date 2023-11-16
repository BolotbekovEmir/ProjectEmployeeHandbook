package kg.mega.projectemployeehandbook.services.employee.impl;

import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.employee.CreateEmployeeDTO;
import kg.mega.projectemployeehandbook.models.entities.Employee;
import kg.mega.projectemployeehandbook.models.entities.Position;
import kg.mega.projectemployeehandbook.models.entities.Structure;
import kg.mega.projectemployeehandbook.models.entities.junction.EmployeePosition;
import kg.mega.projectemployeehandbook.models.entities.junction.EmployeeStatus;
import kg.mega.projectemployeehandbook.models.entities.junction.EmployeeStructure;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.models.enums.FamilyStatus;
import kg.mega.projectemployeehandbook.models.enums.Status;
import kg.mega.projectemployeehandbook.repositories.EmployeeRepository;
import kg.mega.projectemployeehandbook.repositories.PositionRepository;
import kg.mega.projectemployeehandbook.repositories.StructureRepository;
import kg.mega.projectemployeehandbook.repositories.junction.EmployeePositionRepository;
import kg.mega.projectemployeehandbook.repositories.junction.EmployeeStatusRepository;
import kg.mega.projectemployeehandbook.repositories.junction.EmployeeStructureRepository;
import kg.mega.projectemployeehandbook.services.ErrorCollectorService;
import kg.mega.projectemployeehandbook.services.employee.CreateEmployeeService;
import kg.mega.projectemployeehandbook.services.log.LoggingService;
import kg.mega.projectemployeehandbook.services.validation.ValidationUniqueService;
import kg.mega.projectemployeehandbook.utils.CommonRepositoryUtil;
import kg.mega.projectemployeehandbook.utils.EmployeeDateUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.*;
import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CreateEmployeeServiceImpl implements CreateEmployeeService {
    EmployeeStructureRepository employeeStructureRepository;
    EmployeePositionRepository  employeePositionRepository;
    EmployeeStatusRepository    employeeStatusRepository;
    StructureRepository         structureRepository;
    PositionRepository          positionRepository;
    EmployeeRepository          employeeRepository;

    ValidationUniqueService validationUniqueService;
    ErrorCollectorService   errorCollectorService;
    LoggingService          loggingService;

    CommonRepositoryUtil commonRepositoryUtil;
    EmployeeDateUtil     employeeDateUtil;

    @Override
    @Transactional
    public String createEmployee(CreateEmployeeDTO createEmployeeDTO) {
        errorCollectorService.cleanup();

        validateEmployeeData(createEmployeeDTO);
        validateUniqueEmployeeData(createEmployeeDTO);

        employeeDateUtil.validateDatePair(createEmployeeDTO.getStructureStartDate(), createEmployeeDTO.getStructureEndDate());
        employeeDateUtil.validateDatePair(createEmployeeDTO.getPositionStartDate(),  createEmployeeDTO.getPositionEndDate());
        employeeDateUtil.validateDatePair(createEmployeeDTO.getStatusStartDate(),    createEmployeeDTO.getStatusEndDate());

        if (errorCollectorService.getErrorOccurred()) {
            errorCollectorService.callException(
                ExceptionType.CREATE_ENTITY_EXCEPTION
            );
        }

        FamilyStatus familyStatus = checkEnum(
            FamilyStatus.class,
            createEmployeeDTO.getFamilyStatus(),
            ErrorDescription.FAMILY_STATUS_IS_EMPTY
        );

        Status status = checkEnum(
            Status.class,
            createEmployeeDTO.getStatus(),
            ErrorDescription.STATUS_IS_EMPTY
        );

        Structure structure = commonRepositoryUtil.getEntityById(
            createEmployeeDTO.getStructureId(),
            structureRepository,
            ErrorDescription.STRUCTURE_ID_NOT_FOUND
        );

        Position position = commonRepositoryUtil.getEntityById(
            createEmployeeDTO.getPositionId(),
            positionRepository,
            ErrorDescription.POSITION_ID_NOT_FOUND
        );

        Employee employee = employeeBuilder(createEmployeeDTO, familyStatus);

        EmployeeStructure employeeStructure = employeeStructureBuilder(employee, structure, createEmployeeDTO.getStructureStartDate(), createEmployeeDTO.getStructureEndDate());
        EmployeePosition  employeePosition  = employeePositionBuilder(employee, position, createEmployeeDTO.getPositionStartDate(), createEmployeeDTO.getPositionEndDate());
        EmployeeStatus    employeeStatus    = employeeStatusBuilder(employee, status, createEmployeeDTO.getStatusStartDate(), createEmployeeDTO.getStatusEndDate());

        saveEntities(employee, employeeStructure, employeePosition, employeeStatus);

        String operationSuccessMessage = format(InfoDescription.CREATE_EMPLOYEE_FORMAT, employee.getId());
        loggingService.logInfo(operationSuccessMessage);
        return operationSuccessMessage;
    }

    private EmployeeStatus employeeStatusBuilder(
        Employee employee,
        Status status,
        String startDate,
        String endDate
    ) {
        return EmployeeStatus.builder()
            .employee(employee)
            .status(status)
            .startDate(employeeDateUtil.parseOrNow(startDate))
            .endDate(employeeDateUtil.parseDate(endDate))
            .build();
    }

    private EmployeePosition employeePositionBuilder(
        Employee employee,
        Position position,
        String startDate,
        String endDate
    ) {
        return EmployeePosition.builder()
            .employee(employee)
            .position(position)
            .startDate(employeeDateUtil.parseOrNow(startDate))
            .endDate(employeeDateUtil.parseDate(endDate))
            .build();
    }

    private EmployeeStructure employeeStructureBuilder(
        Employee employee,
        Structure structure,
        String startDate,
        String endDate
    ) {
        return EmployeeStructure.builder()
            .employee(employee)
            .structure(structure)
            .startDate(employeeDateUtil.parseOrNow(startDate))
            .endDate(employeeDateUtil.parseDate(endDate))
            .build();
    }

    private Employee employeeBuilder(CreateEmployeeDTO createEmployeeDTO, FamilyStatus familyStatus) {
        return new Employee(
            createEmployeeDTO,
            familyStatus,
            employeeDateUtil.parseDate(createEmployeeDTO.getBirthDate()),
            employeeDateUtil.parseDate(createEmployeeDTO.getEmploymentDate()),
            employeeDateUtil.parseDate(createEmployeeDTO.getDismissalDate())
        );
    }

    private void validateEmployeeData (CreateEmployeeDTO createEmployeeDTO) {
        if (createEmployeeDTO.getPathPhoto().isBlank()) {
            errorCollectorService.addErrorMessages(
                List.of(ErrorDescription.PHOTO_NULL)
            );
        }
        if (createEmployeeDTO.getPatronimyc().isBlank()) {
            createEmployeeDTO.setPatronimyc(null);
        }
    }

    private void validateUniqueEmployeeData(CreateEmployeeDTO createEmployeeDTO) {
        if (createEmployeeDTO.getPersonalNumber().isBlank()) {
            errorCollectorService.addErrorMessages(
                List.of(ErrorDescription.PERSONAL_NUMBER_PATTERN)
            );
        } else {
            if (!validationUniqueService.isUniqueEmployeePersonalNumber(createEmployeeDTO.getPersonalNumber())) {
                errorCollectorService.addErrorMessages(
                    List.of(ErrorDescription.PERSONAL_NUMBER_UNIQUE)
                );
            }
        }
        if (!validationUniqueService.isUniquePhone(createEmployeeDTO.getPhone())) {
            errorCollectorService.addErrorMessages(
                List.of(ErrorDescription.PHONE_UNIQUE)
            );
        }
        if (!validationUniqueService.isUniqueEmail(createEmployeeDTO.getEmail())) {
            errorCollectorService.addErrorMessages(
                List.of(ErrorDescription.EMAIL_UNIQUE)
            );
        }
    }

    private <E extends Enum<E>> E checkEnum(Class<E> enumCLass, String enumName, String errorMessage) {
        E enumObject = commonRepositoryUtil.getEnumByStringName(enumCLass, enumName);

        if (enumObject == null) {
            errorCollectorService.addErrorMessages(
                List.of(errorMessage)
            );
        }

        return enumObject;
    }

    private void saveEntities(
        Employee employee,
        EmployeeStructure employeeStructure,
        EmployeePosition employeePosition,
        EmployeeStatus employeeStatus
    ) {
        employeeRepository.save(employee);
        employeeStructureRepository.save(employeeStructure);
        employeePositionRepository.save(employeePosition);
        employeeStatusRepository.save(employeeStatus);
    }
}