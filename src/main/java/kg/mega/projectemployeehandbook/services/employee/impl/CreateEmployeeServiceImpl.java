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
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.lang.String.*;
import static java.time.LocalDate.*;
import static java.util.List.of;
import static lombok.AccessLevel.PRIVATE;

// TODO: 13.11.2023 отрефакторить говной

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CreateEmployeeServiceImpl implements CreateEmployeeService {
    EmployeeStructureRepository employeeStructureRepository;
    EmployeePositionRepository  employeePositionRepository;
    EmployeeStatusRepository    employeeStatusRepository;
    ValidationUniqueService     validationUniqueService;
    ErrorCollectorService       errorCollectorService;
    CommonRepositoryUtil        commonRepositoryUtil;
    StructureRepository         structureRepository;
    PositionRepository          positionRepository;
    EmployeeRepository          employeeRepository;
    LoggingService              loggingService;

    @Override
    @Transactional
    public String createEmployee(CreateEmployeeDTO createEmployeeDTO) {
        errorCollectorService.cleanup();

        FamilyStatus familyStatus = commonRepositoryUtil.getEnumByStringName(FamilyStatus.class, createEmployeeDTO.getFamilyStatus());

        if (familyStatus == null) {
            errorCollectorService.addErrorMessages(
                of(ErrorDescription.FAMILY_STATUS_IS_EMPTY)
            );
        }

        Status status = commonRepositoryUtil.getEnumByStringName(Status.class, createEmployeeDTO.getStatus());

        if (status == null) {
            errorCollectorService.addErrorMessages(
                of(ErrorDescription.STATUS_IS_EMPTY)
            );
        }

        if (createEmployeeDTO.getPersonalNumber().isBlank()) {
            errorCollectorService.addErrorMessages(
                of(ErrorDescription.PERSONAL_NUMBER_PATTERN)
            );
        } else {
            if (!validationUniqueService.isUniqueEmployeePersonalNumber(createEmployeeDTO.getPersonalNumber())) {
                errorCollectorService.addErrorMessages(
                    of(ErrorDescription.PERSONAL_NUMBER_UNIQUE)
                );
            }
        }

        if (!validationUniqueService.isUniquePhone(createEmployeeDTO.getPhone())) {
            errorCollectorService.addErrorMessages(
                of(ErrorDescription.PHONE_UNIQUE)
            );
        }

        if (!validationUniqueService.isUniqueEmail(createEmployeeDTO.getEmail())) {
            errorCollectorService.addErrorMessages(
                of(ErrorDescription.EMAIL_UNIQUE)
            );
        }

        if (createEmployeeDTO.getPathPhoto().isBlank()) {
            errorCollectorService.addErrorMessages(
                of(ErrorDescription.PHOTO_NULL)
            );
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate
            birthDate      = parse(createEmployeeDTO.getBirthDate(),      formatter),
            employmentDate = parse(createEmployeeDTO.getEmploymentDate(), formatter);

        validateDatePair(createEmployeeDTO.getStructureStartDate(), createEmployeeDTO.getStructureEndDate());
        validateDatePair(createEmployeeDTO.getPositionStartDate(),  createEmployeeDTO.getPositionEndDate());
        validateDatePair(createEmployeeDTO.getStatusStartDate(),    createEmployeeDTO.getStatusEndDate());

        LocalDate
            structureStartDate = startDateParser(createEmployeeDTO.getStructureStartDate(), formatter),
            positionStartDate  = startDateParser(createEmployeeDTO.getPositionStartDate(),  formatter),
            statusStartDate    = startDateParser(createEmployeeDTO.getStatusStartDate(),    formatter),

            structureEndDate   = endDateParser(createEmployeeDTO.getStructureEndDate(), formatter),
            positionEndDate    = endDateParser(createEmployeeDTO.getPositionEndDate(),  formatter),
            statusEndDate      = endDateParser(createEmployeeDTO.getStatusEndDate() ,   formatter),
            dismissalDate      = endDateParser(createEmployeeDTO.getDismissalDate(),    formatter);

        if (errorCollectorService.getErrorOccurred()) {
            errorCollectorService.callException(
                ExceptionType.CREATE_ENTITY_EXCEPTION
            );
        }

        Employee employee = new Employee();

        Structure structure = commonRepositoryUtil.getEntityById(
            createEmployeeDTO.getStructureId(),
            structureRepository,
            ErrorDescription.STRUCTURE_ID_NOT_FOUND,
            ExceptionType.CREATE_ENTITY_EXCEPTION
        );

        Position position = commonRepositoryUtil.getEntityById(
            createEmployeeDTO.getPositionId(),
            positionRepository,
            ErrorDescription.POSITION_ID_NOT_FOUND,
            ExceptionType.CREATE_ENTITY_EXCEPTION
        );

        employee.setFirstname(createEmployeeDTO.getFirstname());
        employee.setLastname(createEmployeeDTO.getLastname());
        employee.setPatronimyc(createEmployeeDTO.getPatronimyc());
        employee.setPersonalNumber(createEmployeeDTO.getPersonalNumber());
        employee.setPhone(createEmployeeDTO.getPhone());
        employee.setEmail(createEmployeeDTO.getEmail());
        employee.setPathPhoto(createEmployeeDTO.getPathPhoto());
        employee.setPostalAddress(createEmployeeDTO.getPostalAddress());
        employee.setFamilyStatus(familyStatus);
        employee.setBirthDate(birthDate);
        employee.setEmploymentDate(employmentDate);
        employee.setDismissalDate(dismissalDate);
        employeeRepository.save(employee);

        EmployeeStructure employeeStructure = new EmployeeStructure();
        employeeStructure.setEmployee(employee);
        employeeStructure.setStructure(structure);
        employeeStructure.setStartDate(structureStartDate);
        employeeStructure.setEndDate(structureEndDate);
        employeeStructureRepository.save(employeeStructure);

        EmployeePosition employeePosition = new EmployeePosition();
        employeePosition.setEmployee(employee);
        employeePosition.setPosition(position);
        employeePosition.setStartDate(positionStartDate);
        employeePosition.setEndDate(positionEndDate);
        employeePositionRepository.save(employeePosition);

        EmployeeStatus employeeStatus = new EmployeeStatus();
        employeeStatus.setEmployee(employee);
        employeeStatus.setStatus(status);
        employeeStatus.setStartDate(statusStartDate);
        employeeStatus.setEndDate(statusEndDate);
        employeeStatusRepository.save(employeeStatus);

        String operationSuccessMessage = format(InfoDescription.CREATE_EMPLOYEE_FORMAT, employee.getId());
        loggingService.logInfo(operationSuccessMessage);
        return operationSuccessMessage;
    }

    private void validateDatePair(String startDate, String endDate) {
        if (!endDate.isBlank() && startDate.isBlank()) {
            errorCollectorService.addErrorMessages(
                of(format(ErrorDescription.END_DATE_IS_PRESENT_BUT_START_DATE_IS_NULL_FORMAT, endDate))
            );
        }
    }

    private LocalDate startDateParser(String startDate, DateTimeFormatter formatter) {
        return startDate.isBlank()
            ? LocalDate.now()
            : parse(startDate, formatter);
    }

    private LocalDate endDateParser(String endDate, DateTimeFormatter formatter) {
        return endDate.isBlank()
            ? null
            : parse(endDate, formatter);
    }
}