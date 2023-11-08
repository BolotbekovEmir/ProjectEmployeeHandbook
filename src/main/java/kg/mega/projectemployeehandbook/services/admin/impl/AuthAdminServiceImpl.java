package kg.mega.projectemployeehandbook.services.admin.impl;

import kg.mega.projectemployeehandbook.models.dto.AuthAdminDTO;
import kg.mega.projectemployeehandbook.models.dto.TokenAdminDTO;
import kg.mega.projectemployeehandbook.services.admin.AuthAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthAdminServiceImpl implements AuthAdminService {

    @Override
    public TokenAdminDTO adminAuth(AuthAdminDTO authAdminDTO) {
        // TODO: 08.11.2023 UserDetails / JWT / Encoder
        return null;
    }

}
