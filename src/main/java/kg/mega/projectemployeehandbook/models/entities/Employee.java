package kg.mega.projectemployeehandbook.models.entities;

import kg.mega.projectemployeehandbook.models.dto.employee.CreateEmployeeDTO;
import kg.mega.projectemployeehandbook.models.enums.FamilyStatus;
import kg.mega.projectemployeehandbook.models.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;

import static lombok.AccessLevel.*;

@Entity
@Table(name = "tb_employee")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = PRIVATE)
public class Employee {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String personalNumber;

    @Column(nullable = false)
    String firstname;

    @Column(nullable = false)
    String lastname;

    String patronimyc;

    @Column(nullable = false)
    String phone;

    @Column(nullable = false)
    String email;

    String postalAddress;

    String pathPhoto;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    Status status;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    FamilyStatus familyStatus;

    @Column(nullable = false)
    LocalDate birthDate;

    @Column(nullable = false)
    LocalDate employmentDate;

    LocalDate dismissalDate;

    public Employee(
        CreateEmployeeDTO createEmployeeDTO,
        FamilyStatus familyStatus,
        Status status,
        LocalDate birthDate,
        LocalDate employmentDate,
        LocalDate dismissalDate
    ) {
        this.firstname      = createEmployeeDTO.getFirstname();
        this.lastname       = createEmployeeDTO.getLastname();
        this.patronimyc     = createEmployeeDTO.getPatronimyc();
        this.personalNumber = createEmployeeDTO.getPersonalNumber();
        this.phone          = createEmployeeDTO.getPhone();
        this.email          = createEmployeeDTO.getEmail();
        this.postalAddress  = createEmployeeDTO.getPostalAddress();
        this.familyStatus   = familyStatus;
        this.status         = status;
        this.birthDate      = birthDate;
        this.employmentDate = employmentDate;
        this.dismissalDate  = dismissalDate;
    }
}
