package kg.mega.projectemployeehandbook.models.entities.junction;

import kg.mega.projectemployeehandbook.models.entities.Employee;
import kg.mega.projectemployeehandbook.models.entities.Position;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

import java.time.LocalDate;

import static lombok.AccessLevel.*;

@Entity
@Table(name = "tb_employee_position")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = PRIVATE)
public class EmployeePosition {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    Employee employee;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    Position position;

    @Column(nullable = false)
    LocalDate startDate;

    LocalDate endDate;

}
