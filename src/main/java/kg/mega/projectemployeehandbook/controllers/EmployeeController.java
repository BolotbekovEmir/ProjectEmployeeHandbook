package kg.mega.projectemployeehandbook.controllers;

import kg.mega.projectemployeehandbook.models.dto.employee.CreateEmployeeDTO;
import kg.mega.projectemployeehandbook.models.dto.employee.EditEmployeePositionDTO;
import kg.mega.projectemployeehandbook.models.dto.employee.EditEmployeeProfileDTO;
import kg.mega.projectemployeehandbook.models.dto.employee.EditEmployeeStructureDTO;
import kg.mega.projectemployeehandbook.models.responses.ApiResult;
import kg.mega.projectemployeehandbook.services.employee.CreateEmployeeService;
import kg.mega.projectemployeehandbook.services.employee.EditEmployeeService;
import kg.mega.projectemployeehandbook.services.employee.SearchEmployeeService;
import kg.mega.projectemployeehandbook.services.employee.SetEmployeeImageService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static lombok.AccessLevel.*;
import static org.springframework.http.HttpStatus.*;

/**
 * Контроллер для управления действиями сотрудников в API v1.
 */
@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EmployeeController {
    SetEmployeeImageService setEmployeeImageService;
    CreateEmployeeService   createEmployeeService;
    SearchEmployeeService   searchEmployeeService;
    EditEmployeeService     editEmployeeService;

    /**
     * Создает нового сотрудника.
     *
     * @param createEmployeeDTO данные для создания сотрудника
     * @return ResponseEntity со строковым результатом операции
     */
    @PostMapping("create")
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


    /**
     * Ищет сотрудников по заданному полю.
     *
     * @param searchField поле для поиска сотрудников
     * @return ResponseEntity со строковым результатом операции
     */
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

    /**
     * Изменяет профиль сотрудника.
     *
     * @param editEmployeeProfileDTO данные для изменения профиля сотрудника
     * @return ResponseEntity со строковым результатом операции
     */
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

    /**
     * Изменяет должность сотрудника.
     *
     * @param editEmployeePositionDTO данные для изменения должности сотрудника
     * @return ResponseEntity со строковым результатом операции
     */
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

    /**
     * Изменяет структуру сотрудника.
     *
     * @param editEmployeeStructureDTO данные для изменения структуры сотрудника
     * @return ResponseEntity с набором GetEmployeeDTO, в случае ненахождения - пустой набор
     */
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

    /**
     * Устанавливает изображение для сотрудника по его персональному номеру.
     *
     * @param personalNumber персональный номер сотрудника
     * @param multipartFile  файл изображения
     * @return ResponseEntity со строковым результатом операции
     */
    @PatchMapping("set-image")
    public ResponseEntity<ApiResult> setImage(@RequestParam String personalNumber, @RequestParam MultipartFile multipartFile) {
        return ResponseEntity.ok().body(
            ApiResult.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .response(
                    setEmployeeImageService.setImage(personalNumber, multipartFile)
                ).build()
        );
    }
}