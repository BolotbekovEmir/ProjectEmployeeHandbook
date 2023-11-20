package kg.mega.projectemployeehandbook.services.admin.impl;

import kg.mega.projectemployeehandbook.errors.ErrorCollector;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.admin.EditAdminDTO;
import kg.mega.projectemployeehandbook.models.entities.Admin;
import kg.mega.projectemployeehandbook.models.enums.AdminRole;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.repositories.AdminRepository;
import kg.mega.projectemployeehandbook.services.admin.EditAdminService;
import kg.mega.projectemployeehandbook.services.log.InfoCollector;
import kg.mega.projectemployeehandbook.services.validation.ValidationUniqueService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.List.of;
import static kg.mega.projectemployeehandbook.models.enums.AdminRole.ADMIN;
import static kg.mega.projectemployeehandbook.models.enums.AdminRole.DISABLE;
import static lombok.AccessLevel.PRIVATE;

/**
 * Сервис для редактирования администратора.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EditAdminServiceImpl implements EditAdminService {
    AdminRepository adminRepository;

    ValidationUniqueService validationUniqueService;

    PasswordEncoder passwordEncoder;
    ErrorCollector  errorCollector;
    InfoCollector   infoCollector;

    /**
     * Метод для редактирования данных администратора.
     *
     * @param editAdminDTO объект, содержащий данные для редактирования администратора
     * @return сообщение об успешном редактировании администратора
     */
    @Override
    @Transactional
    public String editAdmin(EditAdminDTO editAdminDTO) {
        errorCollector.cleanup();
        infoCollector.cleanup();

        String searchedAdminName = editAdminDTO.getSearchedAdminName();
        Admin admin = getAdminFindByName(searchedAdminName);

        if (!validateEditAdmin(editAdminDTO, admin)) {
            errorCollector.callException(ExceptionType.EDIT_ENTITY_EXCEPTION);
        }

        adminRepository.save(Objects.requireNonNull(admin));

        String successfulMessage = format(InfoDescription.EDIT_ADMIN_FORMAT, admin.getId());

        infoCollector.setChangerInfo();
        infoCollector.setEntityInfo(admin.getId(), admin.getAdminName());
        infoCollector.writeFullLog(successfulMessage);

        return format(successfulMessage);
    }

    /**
     * Метод для получения информации об администраторе по имени.
     *
     * @param adminName имя администратора
     * @return объект Admin, соответствующий переданному имени администратора
     * @throws kg.mega.projectemployeehandbook.errors.exceptions.EditEntityException если администратор с переданным именем не найден
     */
    private Admin getAdminFindByName(String adminName) {
        Optional<Admin> optionalAdmin = adminRepository.findByAdminName(adminName);
        if (optionalAdmin.isEmpty()) {
            errorCollector.addErrorMessages(
                of(ErrorDescription.ADMIN_NAME_NOT_FOUND)
            );
            errorCollector.callException(ExceptionType.EDIT_ENTITY_EXCEPTION);
        }
        return optionalAdmin.orElseThrow();
    }

    /**
     * Проверяет данные для редактирования администратора.
     *
     * @param editAdminDTO объект, содержащий данные для редактирования администратора
     * @param admin объект Admin, данные которого редактируются
     * @return true, если данные для редактирования администратора допустимы, иначе false
     */
    private boolean validateEditAdmin(EditAdminDTO editAdminDTO, Admin admin) {
        boolean valid = true;
        valid &= checkAndSetNewAdminName(editAdminDTO.getNewAdminName(), admin);
        valid &= checkAndSetNewPersonalNumber(editAdminDTO.getNewPersonalNumber(), admin);
        valid &= checkAndSetNewPassword(editAdminDTO.getNewPassword(), editAdminDTO.getConfirmNewPassword(), admin);
        valid &= checkAndSetAdminRole(editAdminDTO.getDisableAdmin(), editAdminDTO.getEnableAdmin(), admin.getAdminRole(), admin);
        return valid;
    }

    /**
     * Проверяет и устанавливает новое имя администратора.
     *
     * @param newAdminName новое имя администратора
     * @param admin объект Admin, для которого выполняется проверка и установка нового имени
     * @return true, если новое имя установлено успешно, иначе false
     */
    private boolean checkAndSetNewAdminName(String newAdminName, Admin admin) {
        if (newAdminName.isBlank()) {
            return true;
        }
        if (admin.getAdminName().equals(newAdminName)) {
            errorCollector.addErrorMessages(
                of(ErrorDescription.THIS_ADMIN_NAME_ALREADY_USED)
            );
            return false;
        }
        if (!validationUniqueService.isUniqueAdminName(newAdminName)) {
            errorCollector.addErrorMessages(
                of(ErrorDescription.ADMIN_NAME_UNIQUE)
            );
            return false;
        }

        infoCollector.addFieldUpdatesInfo("adminName", admin.getAdminName(), newAdminName);
        admin.setAdminName(newAdminName);
        return true;
    }

    /**
     * Проверяет и устанавливает новый персональный номер администратора.
     *
     * @param newPersonalNumber новый персональный номер администратора
     * @param admin объект Admin, для которого выполняется проверка и установка нового персонального номера
     * @return true, если новый персональный номер установлен успешно, иначе false
     */
    private boolean checkAndSetNewPersonalNumber(String newPersonalNumber, Admin admin) {
        if (newPersonalNumber.isBlank()) {
            return true;
        }
        if (admin.getPersonalNumber().equals(newPersonalNumber)) {
            errorCollector.addErrorMessages(
                of(ErrorDescription.THIS_ADMIN_PERSONAL_NUMBER_ALREADY_USED)
            );
            return false;
        }
        if (!validationUniqueService.isUniqueAdminPersonalNumber(newPersonalNumber)) {
            errorCollector.addErrorMessages(
                of(ErrorDescription.PERSONAL_NUMBER_UNIQUE)
            );
            return false;
        }
        infoCollector.addFieldUpdatesInfo("personalNumber", admin.getPersonalNumber(), newPersonalNumber);
        admin.setPersonalNumber(newPersonalNumber);
        return true;
    }

    /**
     * Проверяет и устанавливает новый пароль администратора.
     *
     * @param newPassword новый пароль администратора
     * @param confirmNewPassword подтверждение нового пароля администратора
     * @param admin объект Admin, для которого выполняется проверка и установка нового пароля
     * @return true, если новый пароль установлен успешно, иначе false
     */
    private boolean checkAndSetNewPassword(String newPassword, String confirmNewPassword, Admin admin) {
        if (newPassword.isBlank()) {
            return true;
        }
        if (admin.getPassword().equals(newPassword)) {
            errorCollector.addErrorMessages(
                of(ErrorDescription.THIS_PASSWORD_ALREADY_USED)
            );
            return false;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            errorCollector.addErrorMessages(
                of(ErrorDescription.PASSWORDS_EQUAL)
            );
            return false;
        }
        infoCollector.addFieldUpdatesInfo("password", "HIDDEN", "HIDDEN");
        admin.setPassword(passwordEncoder.encode(newPassword));
        return true;
    }

    /**
     * Проверяет и устанавливает новую роль администратора.
     *
     * @param disableAdmin флаг для отключения администратора
     * @param enableAdmin флаг для включения администратора
     * @param currentRole текущая роль администратора
     * @param admin объект Admin, для которого выполняется проверка и установка новой роли
     * @return true, если новая роль установлена успешно, иначе false
     */
    private boolean checkAndSetAdminRole(boolean disableAdmin, boolean enableAdmin, AdminRole currentRole, Admin admin) {
        if (disableAdmin && currentRole.equals(DISABLE)) {
            errorCollector.addErrorMessages(
                of(ErrorDescription.ADMIN_DISABLE)
            );
            return false;
        } else if (disableAdmin) {
            infoCollector.addFieldUpdatesInfo("role", admin.getAdminRole().name(), DISABLE.name());
            admin.setAdminRole(DISABLE);
        }
        if (enableAdmin && currentRole.equals(ADMIN)) {
            errorCollector.addErrorMessages(
                of(ErrorDescription.ADMIN_ENABLE)
            );
            return false;
        } else if (enableAdmin) {
            infoCollector.addFieldUpdatesInfo("role", admin.getAdminRole().name(), ADMIN.name());
            admin.setAdminRole(ADMIN);
        }
        return true;
    }
}