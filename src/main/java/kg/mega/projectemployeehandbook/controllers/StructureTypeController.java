package kg.mega.projectemployeehandbook.controllers;

import kg.mega.projectemployeehandbook.errors.CreateEntityException;
import kg.mega.projectemployeehandbook.errors.EditEntityException;
import kg.mega.projectemployeehandbook.models.dto.structure.CreateStructureTypeDTO;
import kg.mega.projectemployeehandbook.models.dto.structure.EditStructureTypeDTO;
import kg.mega.projectemployeehandbook.models.dto.structure.GetStructureTypeDTO;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;
import kg.mega.projectemployeehandbook.services.structure.CreateStructureTypeService;
import kg.mega.projectemployeehandbook.services.structure.EditStructureTypeService;
import kg.mega.projectemployeehandbook.services.structure.SearchStructureTypeService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static lombok.AccessLevel.*;

@RestController
@RequestMapping("/api/v1/structureType")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class StructureTypeController {
    final CreateStructureTypeService createStructureTypeService;
    final SearchStructureTypeService searchStructureTypeService;
    final EditStructureTypeService   editStructureTypeService;

    @PostMapping("create")
    public RestResponse<CreateEntityException> create(@RequestBody CreateStructureTypeDTO createStructureTypeDTO) {
        return createStructureTypeService.createStructureType(createStructureTypeDTO);
    }

    @PatchMapping("edit")
    public RestResponse<EditEntityException> edit(@RequestBody EditStructureTypeDTO editStructureTypeDTO) {
        return editStructureTypeService.editStructureType(editStructureTypeDTO);
    }

    @GetMapping("findBy")
    public Set<GetStructureTypeDTO> search(@RequestParam String searchField) {
        return searchStructureTypeService.searchStructureType(searchField);
    }

}