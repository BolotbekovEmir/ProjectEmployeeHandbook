package kg.mega.projectemployeehandbook.services.validation.impl;

import kg.mega.projectemployeehandbook.repositories.AdminRepository;
import kg.mega.projectemployeehandbook.repositories.EmployeeRepository;
import kg.mega.projectemployeehandbook.services.validation.ValidationUniqueService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ValidationUniqueServiceImpl implements ValidationUniqueService {
    EmployeeRepository employeeRepository;
    AdminRepository    adminRepository;

    @Override
    public boolean isUniqueAdminName(String adminName) {
        return adminRepository.findByAdminName(adminName).isEmpty();
    }

    @Override
    public boolean isUniqueAdminPersonalNumber(String personalNumber) {
        return adminRepository.findByPersonalNumber(personalNumber).isEmpty();
    }

    @Override
    public boolean isUniqueEmployeePersonalNumber(String personalNumber) {
        return employeeRepository.findByPersonalNumber(personalNumber).isEmpty();
    }

    @Override
    public boolean isUniquePhone(String phone) {
        return employeeRepository.findByPhone(phone).isEmpty();
    }

    @Override
    public boolean isUniqueEmail(String email) {
        return employeeRepository.findByEmail(email).isEmpty();
    }

}