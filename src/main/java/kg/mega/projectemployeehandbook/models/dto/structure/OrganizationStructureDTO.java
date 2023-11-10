package kg.mega.projectemployeehandbook.models.dto.structure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.*;

@Data
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class OrganizationStructureDTO {

    String organizationStructure;

}
