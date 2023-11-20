package kg.mega.projectemployeehandbook.services.employee.impl;

import kg.mega.projectemployeehandbook.errors.ErrorCollector;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.employee.CreateEmployeeDTO;
import kg.mega.projectemployeehandbook.models.entities.Employee;
import kg.mega.projectemployeehandbook.models.entities.Position;
import kg.mega.projectemployeehandbook.models.entities.Structure;
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
import kg.mega.projectemployeehandbook.services.employee.CreateEmployeeService;
import kg.mega.projectemployeehandbook.services.log.InfoCollector;
import kg.mega.projectemployeehandbook.services.validation.ValidationUniqueService;
import kg.mega.projectemployeehandbook.utils.CommonRepositoryUtil;
import kg.mega.projectemployeehandbook.utils.EmployeeDateUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;

/**
 * Сервис для создания сотрудника.
 * */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CreateEmployeeServiceImpl implements CreateEmployeeService {
    EmployeeStructureRepository employeeStructureRepository;
    EmployeePositionRepository  employeePositionRepository;
    StructureRepository         structureRepository;
    PositionRepository          positionRepository;
    EmployeeRepository          employeeRepository;

    ValidationUniqueService validationUniqueService;

    CommonRepositoryUtil commonRepositoryUtil;
    EmployeeDateUtil     employeeDateUtil;
    ErrorCollector       errorCollector;
    InfoCollector        infoCollector;

    /**
     * Создает нового сотрудника на основе предоставленных данных.
     *
     * @param createEmployeeDTO объект, содержащий данные для создания нового сотрудника
     * @return сообщение об успешном завершении операции
     */
    @Override
    @Transactional
    public String createEmployee(CreateEmployeeDTO createEmployeeDTO) {
        errorCollector.cleanup();
        infoCollector.cleanup();

        validateEmployeePatronimyc(createEmployeeDTO);
        validateUniqueEmployeeData(createEmployeeDTO);

        employeeDateUtil.validateDatePair(createEmployeeDTO.getStructureStartDate(), createEmployeeDTO.getStructureEndDate());
        employeeDateUtil.validateDatePair(createEmployeeDTO.getPositionStartDate(), createEmployeeDTO.getPositionEndDate());
        employeeDateUtil.validateDatePair(createEmployeeDTO.getStatusStartDate(), createEmployeeDTO.getStatusEndDate());

        if (errorCollector.getErrorOccurred()) {
            errorCollector.callException(
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

        Employee employee = employeeBuilder(createEmployeeDTO, familyStatus, status);
        EmployeeStructure employeeStructure = employeeStructureBuilder(employee, structure, createEmployeeDTO.getStructureStartDate(), createEmployeeDTO.getStructureEndDate());
        EmployeePosition employeePosition = employeePositionBuilder(employee, position, createEmployeeDTO.getPositionStartDate(), createEmployeeDTO.getPositionEndDate());

        saveEntities(employee, employeeStructure, employeePosition);

        String operationSuccessMessage = format(InfoDescription.CREATE_EMPLOYEE_FORMAT, employee.getId());

        infoCollector.setChangerInfo();
        infoCollector.setEntityInfo(employee.getId(), employee.getPersonalNumber());
        infoCollector.writeLog(operationSuccessMessage);

        return operationSuccessMessage;
    }

    /**
     * Создает и возвращает объект EmployeePosition на основе переданных данных.
     *
     * @param employee   объект Employee
     * @param position   объект Position
     * @param startDate  дата начала
     * @param endDate    дата окончания
     * @return объект EmployeePosition
     */
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
            .endDate(employeeDateUtil.parseOrNull(endDate))
            .build();
    }

    /**
     * Создает и возвращает объект EmployeeStructure на основе переданных данных.
     *
     * @param employee   объект Employee
     * @param structure  объект Structure
     * @param startDate  дата начала
     * @param endDate    дата окончания
     * @return объект EmployeeStructure
     */
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
            .endDate(employeeDateUtil.parseOrNull(endDate))
            .build();
    }

    /**
     * Создает и возвращает объект Employee на основе данных из объекта CreateEmployeeDTO.
     *
     * @param createEmployeeDTO объект CreateEmployeeDTO
     * @param familyStatus      статус семейного положения
     * @param status            статус сотрудника
     * @return объект Employee
     */
    private Employee employeeBuilder(CreateEmployeeDTO createEmployeeDTO, FamilyStatus familyStatus, Status status) {
        return new Employee(
            createEmployeeDTO,
            familyStatus,
            status,
            employeeDateUtil.parseOrNull(createEmployeeDTO.getBirthDate()),
            employeeDateUtil.parseOrNull(createEmployeeDTO.getEmploymentDate()),
            employeeDateUtil.parseOrNull(createEmployeeDTO.getDismissalDate())
        );
    }

    /**
     * Проверяет и обнуляет значение поля "отчество" в объекте CreateEmployeeDTO, если оно пустое.
     *
     * @param createEmployeeDTO объект CreateEmployeeDTO
     */
    private void validateEmployeePatronimyc(CreateEmployeeDTO createEmployeeDTO) {
        if (createEmployeeDTO.getPatronimyc().isBlank()) {
            createEmployeeDTO.setPatronimyc(null);
        }
    }

    /**
     * Проверяет уникальность данных сотрудника перед сохранением.
     *
     * @param createEmployeeDTO объект CreateEmployeeDTO
     */
    private void validateUniqueEmployeeData(CreateEmployeeDTO createEmployeeDTO) {
        if (!validationUniqueService.isUniqueEmployeePersonalNumber(createEmployeeDTO.getPersonalNumber())) {
            errorCollector.addErrorMessages(
                List.of(ErrorDescription.PERSONAL_NUMBER_UNIQUE)
            );
        }
        if (!validationUniqueService.isUniquePhone(createEmployeeDTO.getPhone())) {
            errorCollector.addErrorMessages(
                List.of(ErrorDescription.PHONE_UNIQUE)
            );
        }
        if (!validationUniqueService.isUniqueEmail(createEmployeeDTO.getEmail())) {
            errorCollector.addErrorMessages(
                List.of(ErrorDescription.EMAIL_UNIQUE)
            );
        }
    }

    /**
     * Проверяет строковое значение на соответствие перечислению.
     *
     * @param enumClass    класс перечисления
     * @param enumName     строковое значение для проверки
     * @param errorMessage сообщение об ошибке, если значение не соответствует перечислению
     * @param <E>          тип перечисления
     * @return объект перечисления
     */
    private <E extends Enum<E>> E checkEnum(Class<E> enumClass, String enumName, String errorMessage) {
        E enumObject = commonRepositoryUtil.getEnumByStringName(enumClass, enumName);

        if (enumObject == null) {
            errorCollector.addErrorMessages(
                List.of(errorMessage)
            );
        }

        return enumObject;
    }

    /**
     * Сохраняет сущности Employee, EmployeeStructure и EmployeePosition в репозитории.
     *
     * @param employee          объект Employee
     * @param employeeStructure объект EmployeeStructure
     * @param employeePosition  объект EmployeePosition
     */
    private void saveEntities(
        Employee employee,
        EmployeeStructure employeeStructure,
        EmployeePosition employeePosition
    ) {
        employeeRepository.save(employee);
        employeeStructureRepository.save(employeeStructure);
        employeePositionRepository.save(employeePosition);
    }
}