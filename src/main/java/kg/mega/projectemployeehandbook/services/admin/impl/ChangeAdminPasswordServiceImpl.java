package kg.mega.projectemployeehandbook.services.admin.impl;

import kg.mega.projectemployeehandbook.models.dto.admin.ChangeAdminPasswordDTO;
import kg.mega.projectemployeehandbook.services.admin.ChangeAdminPasswordService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ChangeAdminPasswordServiceImpl implements ChangeAdminPasswordService {
//    final AdminRepository adminRepository;

    @Override
    public String changeAdminPassword(ChangeAdminPasswordDTO changeAdminPasswordDTO) {
        return null;
    }
}