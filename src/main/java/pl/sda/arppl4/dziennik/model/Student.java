package pl.sda.arppl4.dziennik.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private Set<Oceny> oceny;

    private String name;
    private String nazwisko;
    private String numerIndeks;
    private LocalDate dataUrodzenia;

    public Student(String name, String nazwisko, String numerIndeks, LocalDate dataUrodzenia) {
        this.name = name;
        this.nazwisko = nazwisko;
        this.numerIndeks = numerIndeks;
        this.dataUrodzenia = dataUrodzenia;
    }
}
