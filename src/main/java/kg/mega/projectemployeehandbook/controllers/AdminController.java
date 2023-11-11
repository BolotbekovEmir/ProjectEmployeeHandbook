package kg.mega.projectemployeehandbook.controllers;

import kg.mega.projectemployeehandbook.models.dto.admin.*;
import kg.mega.projectemployeehandbook.models.responses.ApiResult;
import kg.mega.projectemployeehandbook.services.admin.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

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
    public ResponseEntity<ApiResult> create(@RequestBody @Valid CreateAdminDTO createAdminDTO) {
        return ResponseEntity.ok().body(
            ApiResult.builder()
                .httpStatus(CREATED)
                .statusCode(CREATED.value())
                .response(
                    createAdminService.createAdmin(createAdminDTO)
                ).build()
        );
    }

    @PatchMapping("edit")
    public ResponseEntity<ApiResult> edit(@RequestBody @Valid EditAdminDTO editAdminDTO) {
        return ResponseEntity.ok().body(
            ApiResult.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .response(
                    editAdminService.editAdmin(editAdminDTO)
                ).build()
        );
    }

    @GetMapping("findBy")
    public ResponseEntity<ApiResult> search(@RequestParam String searchField) {
        return ResponseEntity.ok().body(
            ApiResult.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .response(
                    searchAdminService.searchAdmins(searchField)
                ).build()
        );
    }

    @PatchMapping("changePassword")
    public ResponseEntity<ApiResult> changePassword(@RequestBody @Valid ChangeAdminPasswordDTO changeAdminPasswordDTO) {
        return ResponseEntity.ok().body(
            ApiResult.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .response(
                    changeAdminPasswordService.changeAdminPassword(changeAdminPasswordDTO)
                ).build()
        );
    }

    @GetMapping("auth")
    public ResponseEntity<ApiResult> adminAuth(@RequestBody @Valid AuthAdminDTO authAdminDTO) {
        return ResponseEntity.ok().body(
            ApiResult.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .response(
                    authAdminService.adminAuth(authAdminDTO)
                ).build()
        );
    }

}
