package kg.mega.projectemployeehandbook.services.admin.impl;

import kg.mega.projectemployeehandbook.errors.ErrorCollector;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.admin.ChangeAdminPasswordDTO;
import kg.mega.projectemployeehandbook.models.entities.Admin;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.repositories.AdminRepository;
import kg.mega.projectemployeehandbook.services.admin.ChangeAdminPasswordService;
import kg.mega.projectemployeehandbook.services.log.InfoCollector;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

/**
 * Сервис для изменения пароля администратора.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ChangeAdminPasswordServiceImpl implements ChangeAdminPasswordService {
    AdminRepository adminRepository;

    PasswordEncoder passwordEncoder;
    ErrorCollector  errorCollector;
    InfoCollector   infoCollector;

    /**
     * Метод для изменения пароля администратора.
     *
     * @param changeAdminPasswordDTO объект, содержащий новый пароль администратора
     * @return сообщение об успешном изменении пароля администратора
     */
    @Override
    @Transactional
    public String changePassword(ChangeAdminPasswordDTO changeAdminPasswordDTO) {
        errorCollector.cleanup();
        infoCollector.cleanup();

        String
            newPassword        = changeAdminPasswordDTO.getNewAdminPassword(),
            confirmNewPassword = changeAdminPasswordDTO.getConfirmNewAdminPassword();

        // Проверка, что введенные пароли совпадают
        if (!newPassword.equals(confirmNewPassword)) {
            errorCollector.addErrorMessages(List.of(ErrorDescription.PASSWORDS_EQUAL));
            errorCollector.callException(ExceptionType.EDIT_ENTITY_EXCEPTION);
        }

        // Получение информации об аутентификации пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Проверка, что пользователь аутентифицирован
        if (!authentication.isAuthenticated()) {
            errorCollector.addErrorMessages(List.of(ErrorDescription.ADMIN_HAS_NOT_AUTHENTICATED));
            errorCollector.callException(ExceptionType.GET_ENTITY_EXCEPTION);
        }

        String adminName = authentication.getPrincipal().toString();

        // Поиск администратора по имени
        Optional<Admin> optionalAdmin = adminRepository.findByAdminName(adminName);

        // Обработка случая, если администратор не найден
        if (optionalAdmin.isEmpty()) {
            errorCollector.addErrorMessages(List.of(ErrorDescription.ADMIN_NAME_NOT_FOUND));
            errorCollector.callException(ExceptionType.EDIT_ENTITY_EXCEPTION);
            throw new RuntimeException(); /* [КОСТЫЛЬ] Недостижимая строка, просто чтобы get() не ныл. */
        }

        Admin admin = optionalAdmin.get();

        // Хеширование нового пароля и сохранение его в базу данных
        String encodedPassword = passwordEncoder.encode(newPassword);
        admin.setPassword(encodedPassword);
        adminRepository.save(admin);

        // Запись информации о изменении пароля в лог
        infoCollector.setChangerInfo();
        infoCollector.setEntityInfo(admin.getId(), admin.getAdminName());
        infoCollector.addFieldUpdatesInfo("password", "HIDDEN", "HIDDEN");
        infoCollector.writeFullLog();

        // Возврат сообщения об успешном изменении пароля администратора
        return String.format(
            InfoDescription.ADMIN_PASSWORD_CHANGE_FORMAT, admin.getId()
        );
    }
}