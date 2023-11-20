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
@RequestMapping("/api/v1/admins")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AdminController {
    ChangeAdminPasswordService changeAdminPasswordService;
    CreateAdminService         createAdminService;
    SearchAdminService         searchAdminService;
    AuthAdminService           authAdminService;
    EditAdminService           editAdminService;

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

    @GetMapping("find-by")
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

    @PatchMapping("change-password")
    public ResponseEntity<ApiResult> changePassword(@RequestBody @Valid ChangeAdminPasswordDTO changeAdminPasswordDTO) {
        return ResponseEntity.ok().body(
            ApiResult.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .response(
                    changeAdminPasswordService.changePassword(changeAdminPasswordDTO)
                ).build()
        );
    }

    @PostMapping("auth")
    public ResponseEntity<ApiResult> adminAuth(@RequestBody @Valid AuthAdminDTO authAdminDTO) {
        return ResponseEntity.ok().body(
            ApiResult.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .response(
                    authAdminService.authentication(authAdminDTO)
                ).build()
        );
    }

}
