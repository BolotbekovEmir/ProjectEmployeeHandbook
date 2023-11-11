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

@RestController
@RequestMapping("/api/v1/structureType")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class StructureTypeController {
    final CreateStructureTypeService createStructureTypeService;
    final SearchStructureTypeService searchStructureTypeService;
    final EditStructureTypeService   editStructureTypeService;

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

    @GetMapping("findBy")
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