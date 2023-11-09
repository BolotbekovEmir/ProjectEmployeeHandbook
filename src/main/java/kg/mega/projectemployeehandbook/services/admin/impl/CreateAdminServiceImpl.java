package kg.mega.projectemployeehandbook.services.admin.impl;

import kg.mega.projectemployeehandbook.configuration.MapperConfiguration;
import kg.mega.projectemployeehandbook.errors.CreateEntityException;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.admin.CreateAdminDTO;
import kg.mega.projectemployeehandbook.models.entities.Admin;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;
import kg.mega.projectemployeehandbook.repositories.AdminRepository;
import kg.mega.projectemployeehandbook.services.admin.CreateAdminService;
import kg.mega.projectemployeehandbook.services.log.LoggingService;
import kg.mega.projectemployeehandbook.services.validation.ValidationUniqueService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static kg.mega.projectemployeehandbook.models.enums.AdminRole.*;
import static lombok.AccessLevel.*;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class CreateAdminServiceImpl implements CreateAdminService {
    final ValidationUniqueService validationUniqueService;
    final AdminRepository         adminRepository;
    final LoggingService          loggingService;
    final MapperConfiguration     mapper;

    final RestResponse<CreateEntityException> response = new RestResponse<>();

    @Override
    @Transactional
    public RestResponse<CreateEntityException> createAdmin(CreateAdminDTO createAdminDTO) {
        this.response.setErrorDescriptions(new ArrayList<>());

        if (!isValidCreateAdminData(createAdminDTO)) {
            this.response.setHttpResponse(BAD_REQUEST, BAD_REQUEST.value());
            loggingService.logError(String.format(ErrorDescription.ADMIN_CREATE_INVALID_FORMAT, this.response.getErrorDescriptions()));
            throw new CreateEntityException(this.response.getErrorDescriptions().toString());
        }

        Admin admin = mapper.getMapper().map(createAdminDTO, Admin.class);

        // TODO: 07.11.2023 encoder
        admin.setAdminRole(ADMIN);

        adminRepository.save(admin);
        loggingService.logInfo(String.format(InfoDescription.CREATE_ADMIN_FORMAT, admin.getAdminName()));

        this.response.setHttpResponse(CREATED, CREATED.value());

        return this.response;
    }

    private boolean isValidCreateAdminData(CreateAdminDTO createAdminDTO) {
        if (!validationUniqueService.isUniqueAdminName(createAdminDTO.getAdminName())) {
            this.response.addErrorDescription(ErrorDescription.ADMIN_NAME_UNIQUE);
            return false;
        }

        if (!createAdminDTO.getPersonalNumber().isBlank()) {
            if (!validationUniqueService.isUniqueAdminPersonalNumber(createAdminDTO.getPersonalNumber())) {
                this.response.addErrorDescription(ErrorDescription.PERSONAL_NUMBER_UNIQUE);
                return false;
            }
        } else {
            this.response.addErrorDescription(ErrorDescription.PERSONAL_NUMBER_PATTERN);
            return false;
        }

        if (!createAdminDTO.getPassword().equals(createAdminDTO.getConfirmPassword())) {
            this.response.addErrorDescription(ErrorDescription.PASSWORDS_EQUAL);
            return false;
        }

        return true;
    }

}
