package kg.mega.projectemployeehandbook.services.employee.impl;

import kg.mega.projectemployeehandbook.errors.ErrorCollector;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.entities.Employee;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.repositories.EmployeeRepository;
import kg.mega.projectemployeehandbook.services.employee.SetEmployeeImageService;
import kg.mega.projectemployeehandbook.services.log.InfoCollector;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

/**
 * Сервис для добавления фото сотруднику
 * */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SetEmployeeImageServiceImpl implements SetEmployeeImageService {
    EmployeeRepository employeeRepository;
    ErrorCollector     errorCollector;
    InfoCollector      infoCollector;

    /**
     * Устанавливает изображение для указанного сотрудника на основе переданного файла.
     *
     * @param personalNumber персональный номер сотрудника, для которого устанавливается изображение
     * @param multipartFile файл изображения, который будет установлен для сотрудника
     * @return сообщение об успешном выполнении операции
     */
    @Override
    public String setImage(String personalNumber, MultipartFile multipartFile) {
        errorCollector.cleanup();
        infoCollector.cleanup();

        if (multipartFile.isEmpty()) {
            errorCollector.addErrorMessages(List.of(ErrorDescription.IMAGE_ERROR));
            errorCollector.callException(ExceptionType.EDIT_ENTITY_EXCEPTION);
        }

        try {
            String
                pathImages = "C:\\Users\\User\\IdeaProjects\\ProjectEmployeeHandbook\\src\\main\\resources\\static\\employeephotos",
                imagePath = pathImages.concat(File.separator)
                        .concat(personalNumber)
                        .concat("_")
                        .concat(Objects.requireNonNull(multipartFile.getOriginalFilename()));

            FileSystemResource resource = new FileSystemResource(imagePath);
            multipartFile.transferTo(resource.getFile());

            Optional<Employee> optionalEmployee = employeeRepository.findByPersonalNumber(personalNumber);
            if (optionalEmployee.isEmpty()) {
                errorCollector.addErrorMessages(List.of(ErrorDescription.PERSONAL_NUMBER_NON_EXISTENT));
                errorCollector.callException(ExceptionType.EDIT_ENTITY_EXCEPTION);
                throw new RuntimeException(); /* Недостижимая строка */
            }
            Employee employee = optionalEmployee.get();

            employee.setPathPhoto(imagePath);
            employeeRepository.save(employee);
        } catch (IOException e) {
            errorCollector.addErrorMessages(List.of(ErrorDescription.IMAGE_ERROR));
            errorCollector.callException(ExceptionType.EDIT_ENTITY_EXCEPTION);
        }

        String successfulMessageOperation = String.format(InfoDescription.ADDED_EMPLOYEE_PHOTO_FORMAT, personalNumber);
        infoCollector.setChangerInfo();
        infoCollector.writeLog(successfulMessageOperation);

        return successfulMessageOperation;
    }
}
