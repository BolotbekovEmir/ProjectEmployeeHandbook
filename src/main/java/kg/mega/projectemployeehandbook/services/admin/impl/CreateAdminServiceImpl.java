package kg.mega.projectemployeehandbook.services.admin.impl;

import kg.mega.projectemployeehandbook.configuration.MapperConfiguration;
import kg.mega.projectemployeehandbook.errors.ErrorCollectorService;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.admin.CreateAdminDTO;
import kg.mega.projectemployeehandbook.models.entities.Admin;
import kg.mega.projectemployeehandbook.repositories.AdminRepository;
import kg.mega.projectemployeehandbook.services.admin.CreateAdminService;
import kg.mega.projectemployeehandbook.services.log.LoggingService;
import kg.mega.projectemployeehandbook.services.validation.ValidationUniqueService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;
import static java.util.List.of;
import static kg.mega.projectemployeehandbook.models.enums.AdminRole.ADMIN;
import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CreateAdminServiceImpl implements CreateAdminService {
    ValidationUniqueService validationUniqueService;
    ErrorCollectorService errorCollectorService;
    AdminRepository adminRepository;
    LoggingService loggingService;
    MapperConfiguration mapper;

    @Override
    @Transactional
    public String createAdmin(CreateAdminDTO createAdminDTO) {
        errorCollectorService.cleanup();

        isValidCreateAdminData(createAdminDTO);

        if (errorCollectorService.getErrorOccurred()) {
            errorCollectorService.callException(ExceptionType.CREATE_ENTITY_EXCEPTION);
        }

        Admin admin = mapper.getMapper().map(createAdminDTO, Admin.class);

        // TODO: 07.11.2023 Encoder
        admin.setAdminRole(ADMIN);

        adminRepository.save(admin);

        String operationSuccessMessage = format(InfoDescription.CREATE_ADMIN_FORMAT, admin.getAdminName());
        loggingService.logInfo(operationSuccessMessage);
        return operationSuccessMessage;
    }

    private void isValidCreateAdminData(CreateAdminDTO createAdminDTO) {
        if (!validationUniqueService.isUniqueAdminName(createAdminDTO.getAdminName())) {
            errorCollectorService.addErrorMessages(
                of(ErrorDescription.ADMIN_NAME_UNIQUE)
            );
        }

        if (!validationUniqueService.isUniqueAdminPersonalNumber(createAdminDTO.getPersonalNumber())) {
            errorCollectorService.addErrorMessages(
                of(ErrorDescription.PERSONAL_NUMBER_UNIQUE)
            );
        }

        if (!createAdminDTO.getPassword().equals(createAdminDTO.getConfirmPassword())) {
            errorCollectorService.addErrorMessages(
                of(ErrorDescription.PASSWORDS_EQUAL)
            );
        }
    }
}
