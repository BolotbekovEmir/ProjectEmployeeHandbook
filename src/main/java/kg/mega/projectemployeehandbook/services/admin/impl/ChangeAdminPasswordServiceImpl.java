package kg.mega.projectemployeehandbook.services.admin.impl;

import kg.mega.projectemployeehandbook.errors.EditAdminException;
import kg.mega.projectemployeehandbook.models.dto.ChangeAdminPasswordDTO;
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

    final RestResponse<EditAdminException> response = new RestResponse<>();

    @Override
    public RestResponse<EditAdminException> changeAdminPassword(ChangeAdminPasswordDTO changeAdminPasswordDTO) {
        validatePasswords(changeAdminPasswordDTO.getNewAdminPassword(), changeAdminPasswordDTO.getConfirmNewAdminPassword());

        // TODO: 08.11.2023 UserDetail / JWT / Encoder

        this.response.setHttpResponse(OK, OK.value());
        return this.response;
    }

    private void validatePasswords(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            this.response.setHttpResponse(BAD_REQUEST, BAD_REQUEST.value());
            throw new EditAdminException(this.response.getErrorDescriptions().toString());
        }
    }
}