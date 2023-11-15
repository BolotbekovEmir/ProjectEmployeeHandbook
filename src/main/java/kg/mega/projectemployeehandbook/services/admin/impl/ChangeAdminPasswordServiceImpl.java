package kg.mega.projectemployeehandbook.services.admin.impl;

import kg.mega.projectemployeehandbook.models.dto.admin.ChangeAdminPasswordDTO;
import kg.mega.projectemployeehandbook.services.admin.ChangeAdminPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangeAdminPasswordServiceImpl implements ChangeAdminPasswordService {

    @Override
    public String changeAdminPassword(ChangeAdminPasswordDTO changeAdminPasswordDTO) {

        // TODO: 12.11.2023 UserDetails / JWT / Encoder

        return null;
    }
}