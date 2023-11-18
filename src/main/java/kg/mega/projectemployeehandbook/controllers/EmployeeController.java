package kg.mega.projectemployeehandbook.controllers;

import kg.mega.projectemployeehandbook.models.dto.employee.CreateEmployeeDTO;
import kg.mega.projectemployeehandbook.models.dto.employee.EditEmployeePositionDTO;
import kg.mega.projectemployeehandbook.models.dto.employee.EditEmployeeProfileDTO;
import kg.mega.projectemployeehandbook.models.dto.employee.EditEmployeeStructureDTO;
import kg.mega.projectemployeehandbook.models.responses.ApiResult;
import kg.mega.projectemployeehandbook.services.employee.CreateEmployeeService;
import kg.mega.projectemployeehandbook.services.employee.EditEmployeeService;
import kg.mega.projectemployeehandbook.services.employee.SearchEmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static lombok.AccessLevel.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EmployeeController {
    CreateEmployeeService createEmployeeService;
    SearchEmployeeService searchEmployeeService;
    EditEmployeeService   editEmployeeService;

    @PostMapping
    public ResponseEntity<ApiResult> create(@RequestBody @Valid CreateEmployeeDTO createEmployeeDTO) {
        return ResponseEntity.ok().body(
            ApiResult.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .response(
                    createEmployeeService.createEmployee(createEmployeeDTO)
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
                    searchEmployeeService.searchEmployees(searchField)
                ).build()
        );
    }

    @PatchMapping("edit-profile")
    public ResponseEntity<ApiResult> editProfile(@RequestBody @Valid EditEmployeeProfileDTO editEmployeeProfileDTO) {
        return ResponseEntity.ok().body(
            ApiResult.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .response(
                    editEmployeeService.editEmployeeProfile(editEmployeeProfileDTO)
                ).build()
        );
    }

    @PatchMapping("edit-position")
    public ResponseEntity<ApiResult> editPosition(@RequestBody @Valid EditEmployeePositionDTO editEmployeePositionDTO) {
        return ResponseEntity.ok().body(
            ApiResult.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .response(
                    editEmployeeService.editEmployeePosition(editEmployeePositionDTO)
                ).build()
        );
    }

    @PatchMapping("edit-structure")
    public ResponseEntity<ApiResult> editStructure(@RequestBody @Valid EditEmployeeStructureDTO editEmployeeStructureDTO) {
        return ResponseEntity.ok().body(
            ApiResult.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .response(
                    editEmployeeService.editEmployeeStructure(editEmployeeStructureDTO)
                ).build()
        );
    }
}