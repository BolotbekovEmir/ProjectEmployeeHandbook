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
import kg.mega.projectemployeehandbook.services.log.InfoCollector;
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

/**
 * Сервис для редактирования данных сотрудника.
 */
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

    CommonRepositoryUtil commonRepositoryUtil;
    EmployeeDateUtil     employeeDateUtil;
    ErrorCollector       errorCollector;
    InfoCollector        infoCollector;

    /**
     * Редактирует профиль сотрудника.
     *
     * @param editEmployeeProfileDTO данные для редактирования профиля сотрудника
     * @return сообщение об успешной операции
     */
    @Override
    public String editEmployeeProfile(EditEmployeeProfileDTO editEmployeeProfileDTO) {
        errorCollector.cleanup();
        infoCollector.cleanup();

        String searchedEmployeePersonalNumber = editEmployeeProfileDTO.getPersonalNumber();
        Employee employee = getEmployeeByPersonalNumber(searchedEmployeePersonalNumber);

        validateEditEmployee(editEmployeeProfileDTO, employee);

        if (errorCollector.getErrorOccurred()) {
            errorCollector.callException(ExceptionType.GET_ENTITY_EXCEPTION);
        }

        employeeRepository.save(employee);
        Long employeeId = employee.getId();

        String successfulOperationMessage = String.format(InfoDescription.EDIT_EMPLOYEE_PROFILE_FORMAT, employee.getId());
        infoCollector.setChangerInfo();
        infoCollector.setEntityInfo(employeeId, employee.getPersonalNumber());
        infoCollector.writeFullLog(successfulOperationMessage);

        return successfulOperationMessage;
    }

    /**
     * Валидирует данные редактирования профиля сотрудника.
     *
     * @param editEmployeeProfileDTO данные для редактирования профиля сотрудника
     * @param employee               объект сотрудника
     */
    private void validateEditEmployee(EditEmployeeProfileDTO editEmployeeProfileDTO, Employee employee) {
        checkAndSetFullName(
            editEmployeeProfileDTO.getNewFirstname(),
            editEmployeeProfileDTO.getNewLastname(),
            editEmployeeProfileDTO.getNewPatronimyc(),
            employee
        );
        validateUniqueFields(editEmployeeProfileDTO, employee);
        validatePostalAddress(editEmployeeProfileDTO, employee);
        validateEnumFields(editEmployeeProfileDTO, employee);
        validateDateFields(editEmployeeProfileDTO, employee);
    }

    /**
     * Валидирует уникальные поля сотрудника.
     *
     * @param editEmployeeProfileDTO данные для редактирования профиля сотрудника
     * @param employee               объект сотрудника
     */
    private void validateUniqueFields(EditEmployeeProfileDTO editEmployeeProfileDTO, Employee employee) {
        checkAndSetField(
            "personalNumber",
            employee.getPersonalNumber(),
            editEmployeeProfileDTO.getNewPersonalNumber(),
            employee::setPersonalNumber,
            validationUniqueService::isUniqueEmployeePersonalNumber,
            ErrorDescription.PERSONAL_NUMBER_UNIQUE
        );
        checkAndSetField(
            "phone",
            employee.getPhone(),
            editEmployeeProfileDTO.getNewPhone(),
            employee::setPhone,
            validationUniqueService::isUniquePhone,
            ErrorDescription.PHONE_UNIQUE
        );
        checkAndSetField(
            "email",
            employee.getEmail(),
            editEmployeeProfileDTO.getNewEmail(),
            employee::setEmail,
            validationUniqueService::isUniqueEmail,
            ErrorDescription.EMAIL_UNIQUE
        );
    }

    /**
     * Валидирует почтовый адрес сотрудника.
     *
     * @param editEmployeeProfileDTO данные для редактирования профиля сотрудника
     * @param employee               объект сотрудника
     */
    private void validatePostalAddress(EditEmployeeProfileDTO editEmployeeProfileDTO, Employee employee) {
        if (!editEmployeeProfileDTO.getNewPostalAddress().isBlank()) {
            infoCollector.addFieldUpdatesInfo("postalAddress", employee.getPostalAddress(), editEmployeeProfileDTO.getNewPostalAddress());
            employee.setPostalAddress(editEmployeeProfileDTO.getNewPostalAddress());
        }
    }

    /**
     * Валидирует перечислимые поля сотрудника.
     *
     * @param editEmployeeProfileDTO данные для редактирования профиля сотрудника
     * @param employee               объект сотрудника
     */
    private void validateEnumFields(EditEmployeeProfileDTO editEmployeeProfileDTO, Employee employee) {
        checkAndSetEnumField(
                "familyStatus",
                employee.getFamilyStatus().name(),
                editEmployeeProfileDTO.getNewFamilyStatus(),
                employee::setFamilyStatus,
                FamilyStatus.class
        );
        checkAndSetEnumField(
                "status",
                employee.getStatus().name(),
                editEmployeeProfileDTO.getNewStatus(),
                employee::setStatus,
                Status.class
        );
    }

    /**
     * Валидирует поля даты сотрудника.
     *
     * @param editEmployeeProfileDTO данные для редактирования профиля сотрудника
     * @param employee               объект сотрудника
     */
    private void validateDateFields(EditEmployeeProfileDTO editEmployeeProfileDTO, Employee employee) {
        checkAndSetDate(
                "birthDate",
                employee.getBirthDate().toString(),
                editEmployeeProfileDTO.getNewBirthDate(),
                employee::setBirthDate
        );
        checkAndSetDate(
                "employmentDate",
                employee.getEmploymentDate().toString(),
                editEmployeeProfileDTO.getNewEmploymentDate(),
                employee::setEmploymentDate
        );
        checkAndSetDate(
                "dismissalDate",
                employee.getDismissalDate() != null
                    ? employee.getDismissalDate().toString()
                    : "null",
                editEmployeeProfileDTO.getNewDismissalDate(),
                employee::setDismissalDate
        );
    }

    /**
     * Проверяет и устанавливает полное имя сотрудника.
     *
     * @param newFirstname  новое имя
     * @param newLastname   новая фамилия
     * @param newPatronimyc новое отчество
     * @param employee      объект сотрудника
     */
    private void checkAndSetFullName(String newFirstname, String newLastname, String newPatronimyc, Employee employee) {
        if (!newFirstname.isBlank()) {
            infoCollector.addFieldUpdatesInfo(
                    "firstname",
                    employee.getFirstname(),
                    newFirstname
            );
            employee.setFirstname(newFirstname);
        }

        if (!newLastname.isBlank()) {
            infoCollector.addFieldUpdatesInfo(
                    "lastname",
                    employee.getLastname(),
                    newLastname
            );
            employee.setLastname(newLastname);
        }

        if (!newPatronimyc.isBlank()) {
            infoCollector.addFieldUpdatesInfo(
                    "patronimyc",
                    employee.getPatronimyc(),
                    newPatronimyc
            );
            employee.setPatronimyc(newPatronimyc);
        }
    }

    /**
     * Проверяет и устанавливает новое значение поля, если оно не пустое, иначе добавляет сообщение об ошибке.
     *
     * @param fieldName         имя поля
     * @param oldValue          старое значение поля
     * @param newValue          новое значение поля
     * @param fieldSetter       функция для установки нового значения поля
     * @param uniquenessChecker функция для проверки уникальности нового значения поля
     * @param errorDescription  описание ошибки, если новое значение не уникально
     */
    private void checkAndSetField(
        String fieldName,
        String oldValue,
        String newValue,
        Consumer<String> fieldSetter,
        Function<String, Boolean> uniquenessChecker,
        String errorDescription
    ) {
        if (!newValue.isBlank()) {
            if (uniquenessChecker.apply(newValue)) {
                infoCollector.addFieldUpdatesInfo(fieldName, oldValue, newValue);
                fieldSetter.accept(newValue);
            } else {
                errorCollector.addErrorMessages(List.of(errorDescription));
            }
        }
    }

    /**
     * Проверяет и устанавливает новое значение перечисления, если оно не пустое и существует, иначе добавляет сообщение об ошибке.
     *
     * @param fieldName    имя поля
     * @param oldValue     старое значение поля
     * @param newValue     новое значение поля
     * @param fieldSetter  функция для установки нового значения поля
     * @param enumType     тип перечисления
     * @param <T>          тип перечисления
     */
    private <T extends Enum<T>> void checkAndSetEnumField(
            String fieldName,
            String oldValue,
            String newValue,
            Consumer<T> fieldSetter,
            Class<T> enumType
    ) {
        if (!newValue.isBlank()) {
            T enumValue = commonRepositoryUtil.getEnumByStringName(enumType, newValue);
            if (enumValue == null) {
                errorCollector.addErrorMessages(List.of(ErrorDescription.ENUM_TYPE_NOT_FOUND));
            } else {
                infoCollector.addFieldUpdatesInfo(fieldName, oldValue, newValue);
                fieldSetter.accept(enumValue);
            }
        }
    }

    /**
     * Проверяет и устанавливает новое значение даты, если оно не пустое, иначе оставляет старое значение.
     *
     * @param fieldName  имя поля
     * @param oldDate    старое значение даты
     * @param newDate    новое значение даты
     * @param dateSetter функция для установки нового значения даты
     */
    private void checkAndSetDate(
            String fieldName,
            String oldDate,
            String newDate,
            Consumer<LocalDate> dateSetter
    ) {
        if (newDate != null && !newDate.isBlank()) {
            LocalDate parseDate = employeeDateUtil.parseOrNull(newDate);
            infoCollector.addFieldUpdatesInfo(fieldName, oldDate, newDate);
            dateSetter.accept(parseDate);
        }
    }

    /**
     * Получает сотрудника по его персональному номеру.
     *
     * @param personalNumber персональный номер сотрудника
     * @return объект сотрудника или null, если не найден
     */
    private Employee getEmployeeByPersonalNumber(String personalNumber) {
        Optional<Employee> employee = employeeRepository.findByPersonalNumber(personalNumber);

        if (employee.isEmpty()) {
            errorCollector.addErrorMessages(List.of(ErrorDescription.PERSONAL_NUMBER_NON_EXISTENT));
            errorCollector.callException(ExceptionType.EDIT_ENTITY_EXCEPTION);
        }

        return employee.orElse(null);
    }

    /**
     * Редактирует должность сотрудника.
     *
     * @param editEmployeePositionDTO данные для редактирования должности сотрудника
     * @return сообщение об успешной операции
     */
    @Override
    public String editEmployeePosition(EditEmployeePositionDTO editEmployeePositionDTO) {
        errorCollector.cleanup();
        infoCollector.cleanup();

        String searchedPersonalNumber = editEmployeePositionDTO.getPersonalNumber();
        Employee employee = getEmployeeByPersonalNumber(searchedPersonalNumber);

        if (editEmployeePositionDTO.getIsAddedOperation() && !editEmployeePositionDTO.getIsRemoveOperation()) {
            addedPositionOperation(employee, editEmployeePositionDTO);
        } else if (editEmployeePositionDTO.getIsRemoveOperation() && !editEmployeePositionDTO.getIsAddedOperation()) {
            removePositionOperation(employee, editEmployeePositionDTO);
        } else {
            errorCollector.addErrorMessages(List.of(ErrorDescription.SELECT_OPERATION));
        }

        if (errorCollector.getErrorOccurred()) {
            errorCollector.callException(ExceptionType.EDIT_ENTITY_EXCEPTION);
        }

        String successfulOperationMessage = String.format(InfoDescription.EDIT_EMPLOYEE_PROFILE_FORMAT, employee.getId());
        infoCollector.setChangerInfo();
        infoCollector.writeEntityLog(successfulOperationMessage);

        return successfulOperationMessage;
    }

    /**
     * Добавляет операцию изменения должности сотрудника.
     *
     * @param employee                    объект сотрудника
     * @param editEmployeePositionDTO     данные для изменения должности сотрудника
     */
    private void addedPositionOperation(Employee employee, EditEmployeePositionDTO editEmployeePositionDTO) {
        EmployeePosition employeePosition = new EmployeePosition();
        employeePosition.setEmployee(employee);
        employeePosition.setPosition(
            commonRepositoryUtil.getEntityById(
                editEmployeePositionDTO.getPositionId(),
                positionRepository,
                ErrorDescription.POSITION_ID_NOT_FOUND
            )
        );
        employeePosition.setStartDate(employeeDateUtil.parseOrNow(editEmployeePositionDTO.getStartDate()));
        employeePosition.setEndDate(employeeDateUtil.parseOrNull(editEmployeePositionDTO.getEndDate()));

        employeePositionRepository.save(employeePosition);

        infoCollector.setEntityInfo(
            employeePosition.getId(),
            String.format(
                "employee id %d, position id %d",
                employeePosition.getEmployee().getId(),
                employeePosition.getPosition().getId()
            )
        );
    }

    /**
     * Удаляет операцию изменения должности сотрудника.
     *
     * @param employee                    объект сотрудника
     * @param editEmployeePositionDTO     данные для изменения должности сотрудника
     */
    private void removePositionOperation(Employee employee, EditEmployeePositionDTO editEmployeePositionDTO) {
        Optional<EmployeePosition> optionalEmployeePosition = employeePositionRepository.findByEmployee_PersonalNumberAndPositionId(
            employee.getPersonalNumber(),
            editEmployeePositionDTO.getPositionId()
        );

        if (optionalEmployeePosition.isEmpty()) {
            errorCollector.addErrorMessages(List.of(ErrorDescription.INVALID_THIS_POSITION));
        } else {
            EmployeePosition employeePosition = optionalEmployeePosition.get();

            infoCollector.setEntityInfo(
                employeePosition.getId(),
                String.format(
                    "employee id %d, position id %d",
                    employeePosition.getEmployee().getId(),
                    employeePosition.getPosition().getId()
                )
            );
            infoCollector.addFieldUpdatesInfo(
                "endDate",
                employeePosition.getEndDate() != null
                    ? employeePosition.getEndDate().toString()
                    : "null",
                editEmployeePositionDTO.getEndDate()
            );

            employeePosition.setEndDate(employeeDateUtil.parseOrNow(editEmployeePositionDTO.getEndDate()));
            employeePositionRepository.save(employeePosition);
        }
    }

    /**
     * Редактирует структуру сотрудника.
     *
     * @param editEmployeeStructureDTO данные для редактирования структуры сотрудника
     * @return сообщение об успешной операции
     */
    @Override
    public String editEmployeeStructure(EditEmployeeStructureDTO editEmployeeStructureDTO) {
        errorCollector.cleanup();
        infoCollector.cleanup();

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

        String successfulOperationMessage = String.format(InfoDescription.EDIT_EMPLOYEE_PROFILE_FORMAT, employee.getId());
        infoCollector.setChangerInfo();
        infoCollector.writeEntityLog(successfulOperationMessage);

        return successfulOperationMessage;
    }

    /**
     * Добавляет структуры сотруднику.
     *
     * @param employee                 объект сотрудника
     * @param editEmployeeStructureDTO данные для изменения структуры сотрудника
     */
    private void addedStructureOperation(Employee employee, EditEmployeeStructureDTO editEmployeeStructureDTO) {
        EmployeeStructure employeeStructure = new EmployeeStructure();
        employeeStructure.setEmployee(employee);
        employeeStructure.setStructure(
            commonRepositoryUtil.getEntityById(
                editEmployeeStructureDTO.getStructureId(),
                structureRepository,
                ErrorDescription.STRUCTURE_ID_NOT_FOUND
            )
        );
        employeeStructure.setStartDate(employeeDateUtil.parseOrNow(editEmployeeStructureDTO.getStartDate()));
        employeeStructure.setEndDate(employeeDateUtil.parseOrNull(editEmployeeStructureDTO.getEndDate()));

        employeeStructureRepository.save(employeeStructure);

        infoCollector.setEntityInfo(
            employeeStructure.getId(),
            String.format(
                "employee id %d, structure id %d",
                employeeStructure.getEmployee().getId(),
                employeeStructure.getStructure().getId()
            )
        );
    }

    /**
     * Устанавливает конечные даты структуры сотрудника.
     *
     * @param employee                 объект сотрудника
     * @param editEmployeeStructureDTO данные для изменения структуры сотрудника
     */
    private void removeStructureOperation(Employee employee, EditEmployeeStructureDTO editEmployeeStructureDTO) {
        Optional<EmployeeStructure> optionalEmployeeStructure = employeeStructureRepository.findByEmployee_PersonalNumberAndStructureId(
            employee.getPersonalNumber(),
            editEmployeeStructureDTO.getStructureId()
        );
        if (optionalEmployeeStructure.isEmpty()) {
            errorCollector.addErrorMessages(List.of(ErrorDescription.INVALID_THIS_STRUCTURE));
        } else {
            EmployeeStructure employeeStructure = optionalEmployeeStructure.get();
            infoCollector.setEntityInfo(
                employeeStructure.getId(),
                String.format(
                    "employee id %d, structure id %d",
                    employeeStructure.getEmployee().getId(),
                    employeeStructure.getStructure().getId()
                )
            );
            infoCollector.addFieldUpdatesInfo(
                "endDate",
                employeeStructure.getEndDate() != null
                    ? employeeStructure.getEndDate().toString()
                    : "null",
                editEmployeeStructureDTO.getEndDate()
            );
            employeeStructure.setEndDate(employeeDateUtil.parseOrNow(editEmployeeStructureDTO.getEndDate()));
            employeeStructureRepository.save(employeeStructure);
        }
    }
}