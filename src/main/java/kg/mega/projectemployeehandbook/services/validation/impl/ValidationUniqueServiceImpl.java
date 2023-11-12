package kg.mega.projectemployeehandbook.services.validation.impl;

import kg.mega.projectemployeehandbook.repositories.AdminRepository;
import kg.mega.projectemployeehandbook.services.validation.ValidationUniqueService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ValidationUniqueServiceImpl implements ValidationUniqueService {
    AdminRepository adminRepository;

    @Override
    public boolean isUniqueAdminName(String adminName) {
        return adminRepository.findByAdminName(adminName).isEmpty();
    }

    @Override
    public boolean isUniqueAdminPersonalNumber(String personalNumber) {
        return adminRepository.findByPersonalNumber(personalNumber).isEmpty();
    }

}