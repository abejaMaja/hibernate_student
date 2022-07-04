package pl.sda.arppl4.dziennik.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Oceny {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    Student student;

    private LocalDateTime czasOceny;
    private LocalDateTime czasPoprawki;
    private Double ocena;
    @Enumerated(EnumType.STRING)
    Przedmiot przedmiot;

    public Oceny(Student student, LocalDateTime czasOceny, Double ocena, Przedmiot przedmiot) {
        this.student = student;
        this.czasOceny = czasOceny;
        this.ocena = ocena;
        this.przedmiot = przedmiot;
    }
}
