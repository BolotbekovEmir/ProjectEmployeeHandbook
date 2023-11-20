package kg.mega.projectemployeehandbook.controllers;

import kg.mega.projectemployeehandbook.models.dto.structure.CreateStructureDTO;
import kg.mega.projectemployeehandbook.models.dto.structure.EditStructureDTO;
import kg.mega.projectemployeehandbook.models.responses.ApiResult;
import kg.mega.projectemployeehandbook.services.structure.CreateStructureService;
import kg.mega.projectemployeehandbook.services.structure.EditStructureService;
import kg.mega.projectemployeehandbook.services.structure.SearchStructureService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * Контроллер для управления действиями со структурами в API v1.
 */
@RestController
@RequestMapping("/api/v1/structures")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class StructureController {
    CreateStructureService createStructureService;
    SearchStructureService searchStructureService;
    EditStructureService   editStructureService;

    /**
     * Создает новую структуру.
     *
     * @param createStructureDTO данные для создания структуры
     * @return ResponseEntity со строковым результатом операции
     */
    @PostMapping("create")
    public ResponseEntity<ApiResult> create(@RequestBody CreateStructureDTO createStructureDTO) {
        return ResponseEntity.ok().body(
            ApiResult.builder()
                .httpStatus(CREATED)
                .statusCode(CREATED.value())
                .response(
                    createStructureService.createStructure(createStructureDTO)
                ).build()
        );
    }

    /**
     * Изменяет данные структуры.
     *
     * @param editStructureDTO данные для изменения структуры
     * @return ResponseEntity со строковым результатом операции
     */
    @PatchMapping("edit")
    public ResponseEntity<ApiResult> edit(@RequestBody EditStructureDTO editStructureDTO) {
        return ResponseEntity.ok().body(
            ApiResult.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .response(
                    editStructureService.editStructure(editStructureDTO)
                ).build()
        );
    }

    /**
     * Ищет структуры по заданному полю.
     *
     * @param searchField поле для поиска структур
     * @return ResponseEntity с набором GetStructureDTO, в случае ненахождения - пустой набор
     */
    @GetMapping("find-by")
    public ResponseEntity<ApiResult> search(@RequestParam String searchField) {
        return ResponseEntity.ok().body(
            ApiResult.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .response(
                    searchStructureService.searchStructure(searchField)
                ).build()
        );
    }
}