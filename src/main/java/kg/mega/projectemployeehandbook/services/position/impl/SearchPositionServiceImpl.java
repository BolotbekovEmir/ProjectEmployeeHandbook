package kg.mega.projectemployeehandbook.services.position.impl;

import kg.mega.projectemployeehandbook.models.dto.position.GetPositionDTO;
import kg.mega.projectemployeehandbook.models.entities.Position;
import kg.mega.projectemployeehandbook.repositories.PositionRepository;
import kg.mega.projectemployeehandbook.services.position.SearchPositionService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

import static lombok.AccessLevel.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SearchPositionServiceImpl implements SearchPositionService {
    PositionRepository positionRepository;

    @Override
    public Set<GetPositionDTO> searchPosition(String searchField) {
        Set<Position> searchResult = positionRepository.findAllByPositionNameContainsIgnoreCaseAndActiveIsTrue(searchField);

        return searchResult.stream()
            .map(position -> {
                if (position.getMaster() == null) {
                    return new GetPositionDTO(
                        position.getId(),
                        null,
                        position.getPositionName(),
                        position.getActive()
                    );
                } else {
                    return new GetPositionDTO(
                        position.getId(),
                        position.getMaster().getId(),
                        position.getPositionName(),
                        position.getActive()
                    );
                }
            }
            ).collect(Collectors.toSet());
    }
}