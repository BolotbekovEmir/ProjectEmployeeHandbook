package kg.mega.projectemployeehandbook.services.admin.impl;

import kg.mega.projectemployeehandbook.errors.EditEntityException;
import kg.mega.projectemployeehandbook.models.dto.admin.ChangeAdminPasswordDTO;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;
import kg.mega.projectemployeehandbook.services.admin.ChangeAdminPasswordService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.*;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ChangeAdminPasswordServiceImpl implements ChangeAdminPasswordService {
//    final AdminRepository adminRepository;

    final RestResponse<EditEntityException> response = new RestResponse<>();

    @Override
    public RestResponse<EditEntityException> changeAdminPassword(ChangeAdminPasswordDTO changeAdminPasswordDTO) {
        validatePasswords(changeAdminPasswordDTO.getNewAdminPassword(), changeAdminPasswordDTO.getConfirmNewAdminPassword());

        // TODO: 08.11.2023 UserDetails / JWT / Encoder

        this.response.setHttpResponse(OK, OK.value());
        return this.response;
    }

    private void validatePasswords(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            this.response.setHttpResponse(BAD_REQUEST, BAD_REQUEST.value());
            throw new EditEntityException(this.response.getErrorDescriptions().toString());
        }
    }
}