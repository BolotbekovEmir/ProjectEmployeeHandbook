package kg.mega.projectemployeehandbook.controllers;

import kg.mega.projectemployeehandbook.models.dto.position.CreatePositionDTO;
import kg.mega.projectemployeehandbook.models.dto.position.EditPositionDTO;
import kg.mega.projectemployeehandbook.models.responses.ApiResult;
import kg.mega.projectemployeehandbook.services.position.CreatePositionService;
import kg.mega.projectemployeehandbook.services.position.EditPositionService;
import kg.mega.projectemployeehandbook.services.position.SearchPositionService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.OK;

/**
 * Контроллер для управления действиями с должностями в API v1.
 */
@RestController
@RequestMapping("/api/v1/positions")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class PositionController {
    CreatePositionService createPositionService;
    SearchPositionService searchPositionService;
    EditPositionService   editPositionService;

    /**
     * Создает новую должность.
     *
     * @param createPositionDTO данные для создания должности
     * @return ResponseEntity со строковым результатом операции
     */
    @PostMapping("create")
    public ResponseEntity<ApiResult> create(@RequestBody CreatePositionDTO createPositionDTO) {
        return ResponseEntity.ok().body(
            ApiResult.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .response(
                    createPositionService.createPosition(createPositionDTO)
                ).build()
        );
    }

    /**
     * Изменяет данные должности.
     *
     * @param editPositionDTO данные для изменения должности
     * @return ResponseEntity со строковым результатом операции
     */
    @PatchMapping("edit")
    public ResponseEntity<ApiResult> edit(@RequestBody EditPositionDTO editPositionDTO) {
        return ResponseEntity.ok().body(
            ApiResult.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .response(
                    editPositionService.editPosition(editPositionDTO)
                ).build()
        );
    }

    /**
     * Ищет должности по заданному полю.
     *
     * @param searchField поле для поиска должностей
     * @return ResponseEntity с набор GetPositionDTO, при не нахождении - пустой набор
     */
    @GetMapping("find-by")
    public ResponseEntity<ApiResult> search(@RequestParam String searchField) {
        return ResponseEntity.ok().body(
            ApiResult.builder()
                .httpStatus(OK)
                .statusCode(OK.value())
                .response(
                    searchPositionService.searchPosition(searchField)
                ).build()
        );
    }
}
