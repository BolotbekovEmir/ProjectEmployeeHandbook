package kg.mega.projectemployeehandbook.models.entities.junction;

import kg.mega.projectemployeehandbook.models.entities.Employee;
import kg.mega.projectemployeehandbook.models.entities.Structure;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

import java.time.LocalDate;

import static lombok.AccessLevel.*;

@Entity
@Table(name = "tb_employee_structure")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = PRIVATE)
public class EmployeeStructure {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    Employee employee;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    Structure structure;

    @Column(nullable = false)
    LocalDate startDate;

    LocalDate endDate;

}
