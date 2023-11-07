package kg.mega.projectemployeehandbook.models.entities;

import kg.mega.projectemployeehandbook.models.enums.StatusEmployee;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

import static javax.persistence.EnumType.*;
import static lombok.AccessLevel.*;

@Entity
@Table(name = "tb_status")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = PRIVATE)
public class Status {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    StatusEmployee status;

}
