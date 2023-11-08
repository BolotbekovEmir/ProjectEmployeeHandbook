package kg.mega.projectemployeehandbook.controllers;

import kg.mega.projectemployeehandbook.errors.CreateAdminException;
import kg.mega.projectemployeehandbook.errors.EditAdminException;
import kg.mega.projectemployeehandbook.models.dto.*;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;
import kg.mega.projectemployeehandbook.services.admin.*;
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
    final ChangeAdminPasswordService changeAdminPasswordService;
    final CreateAdminService         createAdminService;
    final SearchAdminService         searchAdminService;
    final AuthAdminService           authAdminService;
    final EditAdminService           editAdminService;

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

    @PatchMapping("changePassword")
    public RestResponse<EditAdminException> changePassword(@RequestBody @Valid ChangeAdminPasswordDTO changeAdminPasswordDTO) {
        return changeAdminPasswordService.changeAdminPassword(changeAdminPasswordDTO);
    }

    @GetMapping("auth")
    public TokenAdminDTO adminAuth(@RequestBody @Valid AuthAdminDTO authAdminDTO) {
        return authAdminService.adminAuth(authAdminDTO);
    }

}
