package kg.mega.projectemployeehandbook.repositories;

import kg.mega.projectemployeehandbook.models.entities.StructureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface StructureTypeRepository extends JpaRepository<StructureType, Long> {

    Set<StructureType> findAllByStructureTypeNameContainsIgnoreCaseAndActiveIsTrue(String structureTypeName);

    Set<StructureType> findAllByActiveIsTrue();

}
