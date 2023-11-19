package kg.mega.projectemployeehandbook.services.employee.impl;

import kg.mega.projectemployeehandbook.errors.ErrorCollector;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.employee.EditEmployeePositionDTO;
import kg.mega.projectemployeehandbook.models.dto.employee.EditEmployeeProfileDTO;
import kg.mega.projectemployeehandbook.models.dto.employee.EditEmployeeStructureDTO;
import kg.mega.projectemployeehandbook.models.entities.Employee;
import kg.mega.projectemployeehandbook.models.entities.junction.EmployeePosition;
import kg.mega.projectemployeehandbook.models.entities.junction.EmployeeStructure;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.models.enums.FamilyStatus;
import kg.mega.projectemployeehandbook.models.enums.Status;
import kg.mega.projectemployeehandbook.repositories.EmployeeRepository;
import kg.mega.projectemployeehandbook.repositories.PositionRepository;
import kg.mega.projectemployeehandbook.repositories.StructureRepository;
import kg.mega.projectemployeehandbook.repositories.junction.EmployeePositionRepository;
import kg.mega.projectemployeehandbook.repositories.junction.EmployeeStructureRepository;
import kg.mega.projectemployeehandbook.services.employee.EditEmployeeService;
import kg.mega.projectemployeehandbook.services.log.LoggingService;
import kg.mega.projectemployeehandbook.services.validation.ValidationUniqueService;
import kg.mega.projectemployeehandbook.utils.CommonRepositoryUtil;
import kg.mega.projectemployeehandbook.utils.EmployeeDateUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EditEmployeeServiceImpl implements EditEmployeeService {
    EmployeeStructureRepository employeeStructureRepository;
    EmployeePositionRepository  employeePositionRepository;
    StructureRepository         structureRepository;
    EmployeeRepository          employeeRepository;
    PositionRepository          positionRepository;

    ValidationUniqueService validationUniqueService;
    LoggingService          loggingService;

    CommonRepositoryUtil commonRepositoryUtil;
    EmployeeDateUtil     employeeDateUtil;
    ErrorCollector       errorCollector;

    @Override
    public String editEmployeeProfile(EditEmployeeProfileDTO editEmployeeProfileDTO) {
        errorCollector.cleanup();

        String searchedEmployeePersonalNumber = editEmployeeProfileDTO.getPersonalNumber();
        Employee employee = getEmployeeByPersonalNumber(searchedEmployeePersonalNumber);

        validateEditEmployee(editEmployeeProfileDTO, employee);

        if (errorCollector.getErrorOccurred()) {
            errorCollector.callException(ExceptionType.GET_ENTITY_EXCEPTION);
        }

        employeeRepository.save(employee);

        return getSuccessMessage(searchedEmployeePersonalNumber);
    }

    private void validateEditEmployee(EditEmployeeProfileDTO editEmployeeProfileDTO, Employee employee) {
        validateFullName(
            editEmployeeProfileDTO.getNewFirstname(),
            editEmployeeProfileDTO.getNewLastname(),
            editEmployeeProfileDTO.getNewPatronimyc(),
            employee
        );
        validateUniqueFields(editEmployeeProfileDTO, employee);
        validateNonEmptyFields(editEmployeeProfileDTO, employee);
        validateEnumFields(editEmployeeProfileDTO, employee);
        validateDateFields(editEmployeeProfileDTO, employee);
    }

    private void validateFullName(String newFirstName, String newLastName, String newPatronymic, Employee employee) {
        checkAndSetFullName(newFirstName, newLastName, newPatronymic, employee);
    }

    private void validateUniqueFields(EditEmployeeProfileDTO editEmployeeProfileDTO, Employee employee) {
        checkAndSetField(
            editEmployeeProfileDTO.getNewPersonalNumber(),
            employee::setPersonalNumber,
            validationUniqueService::isUniqueEmployeePersonalNumber,
            ErrorDescription.PERSONAL_NUMBER_UNIQUE
        );
        checkAndSetField(
            editEmployeeProfileDTO.getNewPhone(),
            employee::setPhone,
            validationUniqueService::isUniquePhone,
            ErrorDescription.PHONE_UNIQUE
        );
        checkAndSetField(
            editEmployeeProfileDTO.getNewEmail(),
            employee::setEmail,
            validationUniqueService::isUniqueEmail,
            ErrorDescription.EMAIL_UNIQUE
        );
    }

    private void validateNonEmptyFields(EditEmployeeProfileDTO editEmployeeProfileDTO, Employee employee) {
        checkAndSetNonEmptyField(editEmployeeProfileDTO.getNewPostalAddress(), employee::setPostalAddress);
        checkAndSetNonEmptyField(editEmployeeProfileDTO.getNewPathPhoto(),     employee::setPathPhoto);
    }

    private void validateEnumFields(EditEmployeeProfileDTO editEmployeeProfileDTO, Employee employee) {
        checkAndSetEnumField(editEmployeeProfileDTO.getNewFamilyStatus(), employee::setFamilyStatus, FamilyStatus.class);
        checkAndSetEnumField(editEmployeeProfileDTO.getNewStatus(),       employee::setStatus, Status.class);
    }

    private void validateDateFields(EditEmployeeProfileDTO editEmployeeProfileDTO, Employee employee) {
        checkAndSetDate(editEmployeeProfileDTO.getNewBirthDate(),      employee::setBirthDate);
        checkAndSetDate(editEmployeeProfileDTO.getNewEmploymentDate(), employee::setEmploymentDate);
        checkAndSetDate(editEmployeeProfileDTO.getNewDismissalDate(),  employee::setDismissalDate);
    }

    private void checkAndSetFullName(String newFirstname, String newLastname, String newPatronimyc, Employee employee) {
        if (!newFirstname.isBlank()) {
            employee.setFirstname(newFirstname);
        }

        if (!newLastname.isBlank()) {
            employee.setLastname(newLastname);
        }

        if (!newPatronimyc.isBlank()) {
            employee.setPatronimyc(newPatronimyc);
        }
    }

    private void checkAndSetField(
        String newValue,
        Consumer<String> fieldSetter,
        Function<String, Boolean> uniquenessChecker,
        String errorDescription
    ) {
        if (!newValue.isBlank()) {
            if (uniquenessChecker.apply(newValue)) {
                fieldSetter.accept(newValue);
            } else {
                errorCollector.addErrorMessages(List.of(errorDescription));
            }
        }
    }

    private void checkAndSetNonEmptyField(String newValue, Consumer<String> fieldSetter) {
        if (!newValue.isBlank()) {
            fieldSetter.accept(newValue);
        }
    }

    private <T extends Enum<T>> void checkAndSetEnumField(String newValue, Consumer<T> fieldSetter, Class<T> enumType) {
        if (!newValue.isBlank()) {
            T enumValue = commonRepositoryUtil.getEnumByStringName(enumType, newValue);
            if (enumValue == null) {
                errorCollector.addErrorMessages(List.of(ErrorDescription.ENUM_TYPE_NOT_FOUND));
            } else {
                fieldSetter.accept(enumValue);
            }
        }
    }

    private void checkAndSetDate(String newDate, Consumer<LocalDate> dateSetter) {
        if (newDate != null && !newDate.isBlank()) {
            LocalDate parseDate = employeeDateUtil.parseOrNull(newDate);
            dateSetter.accept(parseDate);
        }
    }

    private Employee getEmployeeByPersonalNumber(String personalNumber) {
        Optional<Employee> employee = employeeRepository.findByPersonalNumber(personalNumber);

        if (employee.isEmpty()) {
            errorCollector.addErrorMessages(List.of(ErrorDescription.PERSONAL_NUMBER_NON_EXISTENT));
            errorCollector.callException(ExceptionType.EDIT_ENTITY_EXCEPTION);
        }

        return employee.orElse(null);
    }

    private String getSuccessMessage(String personalNumber) {
        String operationSuccessfulMessage = String.format(
            InfoDescription.EDIT_EMPLOYEE_PROFILE_FORMAT, personalNumber
        );
        loggingService.logInfo(operationSuccessfulMessage);
        return operationSuccessfulMessage;
    }

    @Override
    public String editEmployeePosition(EditEmployeePositionDTO editEmployeePositionDTO) {
        errorCollector.cleanup();

        String searchedPersonalNumber = editEmployeePositionDTO.getPersonalNumber();
        Employee employee = getEmployeeByPersonalNumber(searchedPersonalNumber);

        System.out.println(editEmployeePositionDTO);

        if (editEmployeePositionDTO.getIsAddedOperation()) {
            addedPositionOperation(employee, editEmployeePositionDTO);
        } else if (editEmployeePositionDTO.getIsRemoveOperation()) {
            removePositionOperation(employee, editEmployeePositionDTO);
        } else {
            errorCollector.addErrorMessages(List.of(ErrorDescription.SELECT_OPERATION));
        }

        if (errorCollector.getErrorOccurred()) {
            errorCollector.callException(ExceptionType.EDIT_ENTITY_EXCEPTION);
        }

        return getSuccessMessage(searchedPersonalNumber);
    }

    private void addedPositionOperation(Employee employee, EditEmployeePositionDTO editEmployeePositionDTO) {
        EmployeePosition employeePosition = new EmployeePosition();
        employeePosition.setEmployee(employee);
        employeePosition.setPosition(commonRepositoryUtil.getEntityById(
            editEmployeePositionDTO.getPositionId(),
            positionRepository,
            ErrorDescription.POSITION_ID_NOT_FOUND
        ));
        employeePosition.setStartDate(employeeDateUtil.parseOrNow(editEmployeePositionDTO.getStartDate()));
        employeePosition.setEndDate(employeeDateUtil.parseOrNull(editEmployeePositionDTO.getEndDate()));

        employeePositionRepository.save(employeePosition);
    }

    private void removePositionOperation(Employee employee, EditEmployeePositionDTO editEmployeePositionDTO) {
        Optional<EmployeePosition> optionalEmployeePosition = employeePositionRepository.findByEmployee_PersonalNumberAndPositionId(
            employee.getPersonalNumber(),
            editEmployeePositionDTO.getPositionId()
        );

        if (optionalEmployeePosition.isEmpty()) {
            errorCollector.addErrorMessages(List.of(ErrorDescription.INVALID_THIS_POSITION));
        } else {
            optionalEmployeePosition.get().setEndDate(employeeDateUtil.parseOrNow(editEmployeePositionDTO.getEndDate()));
            employeePositionRepository.save(optionalEmployeePosition.get());
        }
    }

    @Override
    public String editEmployeeStructure(EditEmployeeStructureDTO editEmployeeStructureDTO) {
        errorCollector.cleanup();

        String searchedPersonalNumber = editEmployeeStructureDTO.getPersonalNumber();
        Employee employee = getEmployeeByPersonalNumber(searchedPersonalNumber);

        if (editEmployeeStructureDTO.getIsAddedOperation()) {
            addedStructureOperation(employee, editEmployeeStructureDTO);
        } else if (editEmployeeStructureDTO.getIsRemoveOperation()) {
            removeStructureOperation(employee, editEmployeeStructureDTO);
        } else {
            errorCollector.addErrorMessages(List.of(ErrorDescription.SELECT_OPERATION));
        }

        if (errorCollector.getErrorOccurred()) {
            errorCollector.callException(ExceptionType.EDIT_ENTITY_EXCEPTION);
        }

        return getSuccessMessage(searchedPersonalNumber);
    }

    private void addedStructureOperation(Employee employee, EditEmployeeStructureDTO editEmployeeStructureDTO) {
        EmployeeStructure employeeStructure = new EmployeeStructure();
        employeeStructure.setEmployee(employee);
        employeeStructure.setStructure(commonRepositoryUtil.getEntityById(
            editEmployeeStructureDTO.getStructureId(),
            structureRepository,
            ErrorDescription.STRUCTURE_ID_NOT_FOUND
        ));
        employeeStructure.setStartDate(employeeDateUtil.parseOrNow(editEmployeeStructureDTO.getStartDate()));
        employeeStructure.setEndDate(employeeDateUtil.parseOrNull(editEmployeeStructureDTO.getEndDate()));

        employeeStructureRepository.save(employeeStructure);
    }

    private void removeStructureOperation(Employee employee, EditEmployeeStructureDTO editEmployeeStructureDTO) {
        Optional<EmployeeStructure> optionalEmployeeStructure = employeeStructureRepository.findByEmployee_PersonalNumberAndStructureId(
            employee.getPersonalNumber(),
            editEmployeeStructureDTO.getStructureId()
        );
        if (optionalEmployeeStructure.isEmpty()) {
            errorCollector.addErrorMessages(List.of(ErrorDescription.INVALID_THIS_STRUCTURE));
        } else {
            optionalEmployeeStructure.get().setEndDate(employeeDateUtil.parseOrNow(editEmployeeStructureDTO.getEndDate()));
        }
    }
}