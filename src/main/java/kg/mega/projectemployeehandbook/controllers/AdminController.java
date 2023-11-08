package kg.mega.projectemployeehandbook.controllers;

import kg.mega.projectemployeehandbook.errors.CreateAdminException;
import kg.mega.projectemployeehandbook.errors.EditAdminException;
import kg.mega.projectemployeehandbook.models.dto.CreateAdminDTO;
import kg.mega.projectemployeehandbook.models.dto.EditAdminDTO;
import kg.mega.projectemployeehandbook.models.dto.GetAdminDTO;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;
import kg.mega.projectemployeehandbook.services.admin.CreateAdminService;
import kg.mega.projectemployeehandbook.services.admin.EditAdminService;
import kg.mega.projectemployeehandbook.services.admin.SearchAdminService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Set;

import static lombok.AccessLevel.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class AdminController {
    final CreateAdminService createAdminService;
    final SearchAdminService searchAdminService;
    final EditAdminService   editAdminService;

    @PostMapping("create")
    public RestResponse<CreateAdminException> create(@RequestBody @Valid CreateAdminDTO createAdminDTO) {
        return createAdminService.createAdmin(createAdminDTO);
    }

    @PatchMapping("edit")
    public RestResponse<EditAdminException> edit(@RequestBody @Valid EditAdminDTO editAdminDTO) {
        return editAdminService.editAdmin(editAdminDTO);
    }

    @GetMapping("findBy")
    public Set<GetAdminDTO> search(@RequestParam String searchField) {
        return searchAdminService.searchAdmins(searchField);
    }

}
