package kg.mega.projectemployeehandbook.models.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

import static lombok.AccessLevel.*;

@Entity
@Table(name = "tb_position")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = PRIVATE)
public class Position {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    Position master;

    @Column(nullable = false)
    String positionName;

    @Column(nullable = false)
    Boolean active;

}