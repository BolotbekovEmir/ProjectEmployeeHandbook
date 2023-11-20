package kg.mega.projectemployeehandbook.controllers;

import kg.mega.projectemployeehandbook.models.dto.structuretype.CreateStructureTypeDTO;
import kg.mega.projectemployeehandbook.models.dto.structuretype.EditStructureTypeDTO;
import kg.mega.projectemployeehandbook.models.responses.ApiResult;
import kg.mega.projectemployeehandbook.services.structuretype.CreateStructureTypeService;
import kg.mega.projectemployeehandbook.services.structuretype.EditStructureTypeService;
import kg.mega.projectemployeehandbook.services.structuretype.SearchStructureTypeService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static lombok.AccessLevel.*;
import static org.springframework.http.HttpStatus.*;

/**
 * Контроллер для управления типами структур в API v1.
 */
@RestController
@RequestMapping("/api/v1/structure-types")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class StructureTypeController {
    CreateStructureTypeService createStructureTypeService;
    SearchStructureTypeService searchStructureTypeService;
    EditStructureTypeService   editStructureTypeService;

    /**
     * Создает новый тип структуры.
     *
     * @param createStructureTypeDTO данные для создания типа структуры
     * @return ResponseEntity со строковым результатом операции
     */
    @PostMapping("create")
    public ResponseEntity<ApiResult> create(@RequestBody CreateStructureTypeDTO createStructureTypeDTO) {
        return ResponseEntity.ok().body(
            ApiResult.builder()
                .httpStatus(CREATED)
                .statusCode(CREATED.value())
                .response(
                    createStructureTypeService.createStructureType(createStructureTypeDTO)
                ).build()
        );
    }

    /**
     * Изменяет данные типа структуры.
     *
     * @param editStructureTypeDTO данные для изменения типа структуры
     * @return ResponseEntity со строковым результатом операции
     */
    @PatchMapping("edit")
    public ResponseEntity<ApiResult> edit(@RequestBody EditStructureTypeDTO editStructureTypeDTO) {
        return ResponseEntity.ok().body(
            ApiResult.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .response(
                    editStructureTypeService.editStructureType(editStructureTypeDTO)
                ).build()
        );
    }

    /**
     * Ищет типы структур по заданному полю.
     *
     * @param searchField поле для поиска типов структур
     * @return ResponseEntity с набором GetStructureTypeDTO, в случае ненахождения - пустой набор
     */
    @GetMapping("find-by")
    public ResponseEntity<ApiResult> search(@RequestParam String searchField) {
        return ResponseEntity.ok().body(
            ApiResult.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .response(
                    searchStructureTypeService.searchStructureType(searchField)
                ).build()
        );
    }
}