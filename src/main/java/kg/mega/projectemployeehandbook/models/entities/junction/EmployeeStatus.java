package kg.mega.projectemployeehandbook.models.entities.junction;

import kg.mega.projectemployeehandbook.models.entities.Employee;
import kg.mega.projectemployeehandbook.models.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

import java.time.LocalDate;

import static lombok.AccessLevel.*;

@Entity
@Table(name = "tb_employee_status")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = PRIVATE)
public class EmployeeStatus {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    Employee employee;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    Status status;

    @Column(nullable = false)
    LocalDate startDate;

    LocalDate endDate;

}
