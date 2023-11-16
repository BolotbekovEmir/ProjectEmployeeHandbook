package kg.mega.projectemployeehandbook.controllers;

import kg.mega.projectemployeehandbook.models.dto.employee.CreateEmployeeDTO;
import kg.mega.projectemployeehandbook.models.responses.ApiResult;
import kg.mega.projectemployeehandbook.services.employee.CreateEmployeeService;
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

    /* Создание сотрудника */
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

    @PatchMapping
    public ResponseEntity<ApiResult> edit() {
        return null;
    }

}