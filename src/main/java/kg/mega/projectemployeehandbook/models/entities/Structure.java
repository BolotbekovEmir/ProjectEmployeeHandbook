package kg.mega.projectemployeehandbook.models.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

import static lombok.AccessLevel.*;

@Entity
@Table(name = "tb_structure")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = PRIVATE)
public class Structure {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    Structure master;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    StructureType structureType;

    @Column(nullable = false)
    String structureName;

    @Column(nullable = false)
    Boolean active;

    public Structure(Structure master, StructureType structureType, String structureName, Boolean active) {
        this.master = master;
        this.structureType = structureType;
        this.structureName = structureName;
        this.active = active;
    }
}
