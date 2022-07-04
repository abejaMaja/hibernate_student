package pl.sda.arppl4.dziennik.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Ocena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Student student;

    private LocalDateTime czasOceny;
    private LocalDateTime czasPoprawki;
    private Double ocena;
    @Enumerated(EnumType.STRING)
    private Przedmiot przedmiot;

    public Ocena(Student student, Double ocena, Przedmiot przedmiot) {
        this.student = student;
        this.ocena = ocena;
        this.przedmiot = przedmiot;
    }
}
