package kg.mega.projectemployeehandbook.services.validation.impl;

import kg.mega.projectemployeehandbook.repositories.AdminRepository;
import kg.mega.projectemployeehandbook.repositories.EmployeeRepository;
import kg.mega.projectemployeehandbook.services.validation.ValidationUniqueService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.*;

/**
 * Реализация сервиса проверки уникальности данных.
 * Предоставляет методы для проверки уникальности имен, персональных номеров, телефонов и электронных адресов
 * администраторов и сотрудников в соответствующих репозиториях.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ValidationUniqueServiceImpl implements ValidationUniqueService {
    EmployeeRepository employeeRepository;
    AdminRepository    adminRepository;

    /**
     * Проверяет уникальность имени администратора.
     * @param adminName Имя администратора для проверки.
     * @return true, если имя администратора уникально, в противном случае - false.
     */
    @Override
    public boolean isUniqueAdminName(String adminName) {
        return adminRepository.findByAdminName(adminName).isEmpty();
    }

    /**
     * Проверяет уникальность персонального номера администратора.
     * @param personalNumber Персональный номер администратора для проверки.
     * @return true, если персональный номер администратора уникальный, в противном случае - false.
     */
    @Override
    public boolean isUniqueAdminPersonalNumber(String personalNumber) {
        return adminRepository.findByPersonalNumber(personalNumber).isEmpty();
    }

    /**
     * Проверяет уникальность персонального номера сотрудника.
     * @param personalNumber Персональный номер сотрудника для проверки.
     * @return true, если персональный номер сотрудника уникальный, в противном случае - false.
     */
    @Override
    public boolean isUniqueEmployeePersonalNumber(String personalNumber) {
        return employeeRepository.findByPersonalNumber(personalNumber).isEmpty();
    }

    /**
     * Проверяет уникальность телефона сотрудника.
     * @param phone Телефон для проверки.
     * @return true, если телефон уникальный, в противном случае - false.
     */
    @Override
    public boolean isUniquePhone(String phone) {
        return employeeRepository.findByPhone(phone).isEmpty();
    }

    /**
     * Проверяет уникальность электронного адреса сотрудника.
     * @param email Электронный адрес для проверки.
     * @return true, если электронный адрес уникальный, в противном случае - false.
     */
    @Override
    public boolean isUniqueEmail(String email) {
        return employeeRepository.findByEmail(email).isEmpty();
    }
}