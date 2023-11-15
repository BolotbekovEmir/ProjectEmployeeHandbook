package kg.mega.projectemployeehandbook.models.entities;

import kg.mega.projectemployeehandbook.models.enums.FamilyStatus;
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
@ToString
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

    @Column(nullable = false)
    String pathPhoto;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    FamilyStatus familyStatus;

    @Column(nullable = false)
    LocalDate birthDate;

    @Column(nullable = false)
    LocalDate employmentDate;

    LocalDate dismissalDate;

}
