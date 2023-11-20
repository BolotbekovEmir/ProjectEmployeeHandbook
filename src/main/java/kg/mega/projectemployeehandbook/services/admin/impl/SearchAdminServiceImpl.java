package kg.mega.projectemployeehandbook.services.admin.impl;

import kg.mega.projectemployeehandbook.configuration.MapperConfiguration;
import kg.mega.projectemployeehandbook.models.dto.admin.GetAdminDTO;
import kg.mega.projectemployeehandbook.models.entities.Admin;
import kg.mega.projectemployeehandbook.models.enums.AdminRole;
import kg.mega.projectemployeehandbook.repositories.AdminRepository;
import kg.mega.projectemployeehandbook.services.admin.SearchAdminService;
import kg.mega.projectemployeehandbook.utils.CommonRepositoryUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static lombok.AccessLevel.*;

/**
 * Сервис для поиска администраторов.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SearchAdminServiceImpl implements SearchAdminService {
    CommonRepositoryUtil commonRepositoryUtil;
    AdminRepository      adminRepository;

    MapperConfiguration mapper;

    /**
     * Метод для поиска администраторов по заданному критерию.
     *
     * @param searchField строка-критерий поиска администраторов
     * @return множество объектов GetAdminDTO, соответствующих результатам поиска
     */
    @Override
    public Set<GetAdminDTO> searchAdmins(String searchField) {
        // Поиск администраторов по имени, персональному номеру и роли
        Set<Admin>
            adminsByName           = adminRepository.findAllByAdminNameContainsIgnoreCase(searchField),
            adminsByPersonalNumber = adminRepository.findAllByPersonalNumber(searchField),
            searchResult           = new HashSet<>();

        // Поиск администраторов по роли, если переданный критерий является значением перечисления AdminRole
        AdminRole adminRole = commonRepositoryUtil.getEnumByStringName(AdminRole.class, searchField);
        if (adminRole != null) {
            Set<Admin> adminsByRole = adminRepository.findAllByAdminRole(adminRole);
            searchResult.addAll(adminsByRole);
        }

        // Объединение результатов поиска
        searchResult.addAll(adminsByName);
        searchResult.addAll(adminsByPersonalNumber);

        // Преобразование найденных администраторов в объекты GetAdminDTO
        return searchResult.stream()
            .map(admin -> mapper.getMapper()
                .map(admin, GetAdminDTO.class))
            .collect(Collectors.toSet());
    }
}