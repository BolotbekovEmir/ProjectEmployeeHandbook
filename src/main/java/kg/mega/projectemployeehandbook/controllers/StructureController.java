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

@RestController
@RequestMapping("/api/v1/structure")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class StructureController {
    final CreateStructureService createStructureService;
    final SearchStructureService searchStructureService;
    final EditStructureService   editStructureService;

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

    @GetMapping("search")
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