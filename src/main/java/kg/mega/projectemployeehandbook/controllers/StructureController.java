package kg.mega.projectemployeehandbook.controllers;

import kg.mega.projectemployeehandbook.errors.CreateEntityException;
import kg.mega.projectemployeehandbook.errors.EditEntityException;
import kg.mega.projectemployeehandbook.models.dto.structure.CreateStructureDTO;
import kg.mega.projectemployeehandbook.models.dto.structure.EditStructureDTO;
import kg.mega.projectemployeehandbook.models.dto.structure.OrganizationStructureDTO;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;
import kg.mega.projectemployeehandbook.services.structure.CreateStructureService;
import kg.mega.projectemployeehandbook.services.structure.EditStructureService;
import kg.mega.projectemployeehandbook.services.structure.SearchStructureService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import static lombok.AccessLevel.*;

@RestController
@RequestMapping("/api/v1/structure")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class StructureController {
    final CreateStructureService createStructureService;
    final SearchStructureService searchStructureService;
    final EditStructureService   editStructureService;

    @PostMapping("create")
    public RestResponse<CreateEntityException> create(@RequestBody CreateStructureDTO createStructureDTO) {
        return createStructureService.createStructure(createStructureDTO);
    }

    @PatchMapping("edit")
    public RestResponse<EditEntityException> edit(@RequestBody EditStructureDTO editStructureDTO) {
        return editStructureService.editStructure(editStructureDTO);
    }

    @GetMapping("search")
    public OrganizationStructureDTO search(@RequestParam Long structureId) {
        return searchStructureService.searchStructure(structureId);
    }

}
