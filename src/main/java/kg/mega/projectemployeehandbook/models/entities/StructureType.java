package kg.mega.projectemployeehandbook.models.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

import static lombok.AccessLevel.*;

@Entity
@Table(name = "tb_structure_type")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = PRIVATE)
public class StructureType {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String structureTypeName;

    @Column(nullable = false)
    Boolean active;

}
