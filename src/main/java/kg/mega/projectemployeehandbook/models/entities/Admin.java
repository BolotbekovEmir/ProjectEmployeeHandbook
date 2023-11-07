package kg.mega.projectemployeehandbook.models.entities;

import kg.mega.projectemployeehandbook.models.enums.AdminRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

import static lombok.AccessLevel.*;

@Entity
@Table(name = "tb_admin")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = PRIVATE)
public class Admin {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String adminName;

    @Column(nullable = false)
    String password;

    @Column(nullable = false)
    String personalNumber;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    AdminRole adminRole;

}
