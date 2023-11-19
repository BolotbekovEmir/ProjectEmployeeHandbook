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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SearchAdminServiceImpl implements SearchAdminService {
    CommonRepositoryUtil commonRepositoryUtil;
    AdminRepository      adminRepository;

    MapperConfiguration mapper;

    @Override
    public Set<GetAdminDTO> searchAdmins(String searchField) {
        System.out.println(searchField);
        Set<Admin>
            adminsByName           = adminRepository.findAllByAdminNameContainsIgnoreCase(searchField),
            adminsByPersonalNumber = adminRepository.findAllByPersonalNumber(searchField),
            searchResult           = new HashSet<>();

        AdminRole adminRole = commonRepositoryUtil.getEnumByStringName(AdminRole.class, searchField);

        if (adminRole != null) {
            Set<Admin> adminsByRole = adminRepository.findAllByAdminRole(adminRole);
            searchResult.addAll(adminsByRole);
        }

        searchResult.addAll(adminsByName);
        searchResult.addAll(adminsByPersonalNumber);

        return searchResult.stream()
            .map(admin -> mapper.getMapper()
                .map(admin, GetAdminDTO.class))
            .collect(Collectors.toSet());
    }
}