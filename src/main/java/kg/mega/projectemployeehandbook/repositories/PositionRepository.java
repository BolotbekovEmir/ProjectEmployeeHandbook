package kg.mega.projectemployeehandbook.repositories;

import kg.mega.projectemployeehandbook.models.entities.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface PositionRepository extends JpaRepository<Position, Long> {

    Set<Position> findAllByPositionNameContainsIgnoreCaseAndActiveIsTrue(String positionName);

}
