package kg.mega.projectemployeehandbook.controllers;

import kg.mega.projectemployeehandbook.errors.CreateEntityException;
import kg.mega.projectemployeehandbook.errors.EditEntityException;
import kg.mega.projectemployeehandbook.models.dto.GetPositionDTO;
import kg.mega.projectemployeehandbook.models.dto.position.CreatePositionDTO;
import kg.mega.projectemployeehandbook.models.dto.position.EditPositionDTO;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;
import kg.mega.projectemployeehandbook.services.position.CreatePositionService;
import kg.mega.projectemployeehandbook.services.position.EditPositionService;
import kg.mega.projectemployeehandbook.services.position.SearchPositionService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static lombok.AccessLevel.*;

@RestController
@RequestMapping("/api/v1/position")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class PositionController {
    final CreatePositionService createPositionService;
    final SearchPositionService searchPositionService;
    final EditPositionService   editPositionService;

    @PostMapping("create")
    public RestResponse<CreateEntityException> create(@RequestBody CreatePositionDTO createPositionDTO) {
        return createPositionService.createPosition(createPositionDTO);
    }

    @PatchMapping("edit")
    public RestResponse<EditEntityException> edit(@RequestBody EditPositionDTO editPositionDTO) {
        return editPositionService.editPosition(editPositionDTO);
    }

    @GetMapping("findBy")
    public Set<GetPositionDTO> search(@RequestParam String searchField) {
        return searchPositionService.searchPosition(searchField);
    }

}
