package kg.mega.projectemployeehandbook.services.admin.impl;

import kg.mega.projectemployeehandbook.configuration.MapperConfiguration;
import kg.mega.projectemployeehandbook.errors.ErrorCollector;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.admin.CreateAdminDTO;
import kg.mega.projectemployeehandbook.models.entities.Admin;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.repositories.AdminRepository;
import kg.mega.projectemployeehandbook.services.admin.CreateAdminService;
import kg.mega.projectemployeehandbook.services.log.InfoCollector;
import kg.mega.projectemployeehandbook.services.validation.ValidationUniqueService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;
import static java.util.List.of;
import static kg.mega.projectemployeehandbook.models.enums.AdminRole.ADMIN;
import static lombok.AccessLevel.PRIVATE;

/**
 * Сервис для создания администратора.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CreateAdminServiceImpl implements CreateAdminService {
    AdminRepository adminRepository;

    ValidationUniqueService validationUniqueService;

    MapperConfiguration mapper;
    PasswordEncoder     passwordEncoder;
    ErrorCollector      errorCollector;
    InfoCollector       infoCollector;

    /**
     * Метод для создания администратора.
     *
     * @param createAdminDTO объект, содержащий данные для создания администратора
     * @return сообщение об успешном создании администратора
     */
    @Override
    @Transactional
    public String createAdmin(CreateAdminDTO createAdminDTO) {
        errorCollector.cleanup();
        infoCollector.cleanup();

        isValidCreateAdminData(createAdminDTO);

        // Проверка наличия ошибок при валидации данных
        if (errorCollector.getErrorOccurred()) {
            errorCollector.callException(ExceptionType.CREATE_ENTITY_EXCEPTION);
        }

        Admin admin = mapper.getMapper().map(createAdminDTO, Admin.class);

        admin.setAdminRole(ADMIN);

        // Хэширование пароля и сохранение в базу
        admin.setPassword(passwordEncoder.encode(createAdminDTO.getPassword()));
        adminRepository.save(admin);

        // Запись информации о создании администратора в лог
        infoCollector.setChangerInfo();
        infoCollector.writeLog(String.format(InfoDescription.CREATE_ADMIN_FORMAT, admin.getId()));

        return format(InfoDescription.CREATE_ADMIN_FORMAT, admin.getId());
    }

    /**
     * Метод для проверки валидности данных при создании администратора.
     *
     * @param createAdminDTO объект, содержащий данные для создания администратора
     */
    private void isValidCreateAdminData(CreateAdminDTO createAdminDTO) {
        if (!validationUniqueService.isUniqueAdminName(createAdminDTO.getAdminName())) {
            errorCollector.addErrorMessages(
                of(ErrorDescription.ADMIN_NAME_UNIQUE)
            );
        }

        if (!validationUniqueService.isUniqueAdminPersonalNumber(createAdminDTO.getPersonalNumber())) {
            errorCollector.addErrorMessages(
                of(ErrorDescription.PERSONAL_NUMBER_UNIQUE)
            );
        }

        if (!createAdminDTO.getPassword().equals(createAdminDTO.getConfirmPassword())) {
            errorCollector.addErrorMessages(
                of(ErrorDescription.PASSWORDS_EQUAL)
            );
        }
    }
}
