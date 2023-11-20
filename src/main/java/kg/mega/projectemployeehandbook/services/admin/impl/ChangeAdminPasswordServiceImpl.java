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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ChangeAdminPasswordServiceImpl implements ChangeAdminPasswordService {
    AdminRepository adminRepository;

    PasswordEncoder passwordEncoder;
    ErrorCollector  errorCollector;
    InfoCollector   infoCollector;

    @Override
    @Transactional
    public String changePassword(ChangeAdminPasswordDTO changeAdminPasswordDTO) {
        errorCollector.cleanup();
        infoCollector.cleanup();

        String
            newPassword        = changeAdminPasswordDTO.getNewAdminPassword(),
            confirmNewPassword = changeAdminPasswordDTO.getConfirmNewAdminPassword();

        if (!newPassword.equals(confirmNewPassword)) {
            errorCollector.addErrorMessages(List.of(ErrorDescription.PASSWORDS_EQUAL));
            errorCollector.callException(ExceptionType.EDIT_ENTITY_EXCEPTION);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.isAuthenticated()) {
            errorCollector.addErrorMessages(List.of(ErrorDescription.ADMIN_HAS_NOT_AUTHENTICATED));
            errorCollector.callException(ExceptionType.GET_ENTITY_EXCEPTION);
        }

        String adminName = authentication.getPrincipal().toString();

        Optional<Admin> optionalAdmin = adminRepository.findByAdminName(adminName);

        if (optionalAdmin.isEmpty()) {
            errorCollector.addErrorMessages(List.of(ErrorDescription.ADMIN_NAME_NOT_FOUND));
            errorCollector.callException(ExceptionType.EDIT_ENTITY_EXCEPTION);
            throw new RuntimeException(); /* [КОСТЫЛЬ] Недостижимая строка, просто чтобы get() не ныл. */
        }

        Admin admin = optionalAdmin.get();

        String encodedPassword = passwordEncoder.encode(newPassword);
        admin.setPassword(encodedPassword);

        adminRepository.save(admin);

        infoCollector.setChangerInfo();
        infoCollector.setEntityInfo(admin.getId(), admin.getAdminName());
        infoCollector.addFieldUpdatesInfo("password", "HIDDEN", "HIDDEN");
        infoCollector.writeFullLog();

        return String.format(
            InfoDescription.ADMIN_PASSWORD_CHANGE, admin.getId()
        );
    }
}