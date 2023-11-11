package kg.mega.projectemployeehandbook.repositories;

import kg.mega.projectemployeehandbook.models.entities.Structure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface StructureRepository extends JpaRepository<Structure, Long> {

    Set<Structure> findAllByStructureNameContainsIgnoreCaseAndActiveIsTrue(String structureName);

}
