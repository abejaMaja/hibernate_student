package pl.sda.arppl4.dziennik.parser;

import lombok.Data;
import pl.sda.arppl4.dziennik.dao.GenericDao;
import pl.sda.arppl4.dziennik.model.Oceny;
import pl.sda.arppl4.dziennik.model.Przedmiot;
import pl.sda.arppl4.dziennik.model.Student;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

public class StudentParser {


    private final Scanner scanner;
    private final GenericDao<Student> daoStudent;
    private final GenericDao<Oceny> daoOcena;


    public StudentParser(Scanner scanner, GenericDao<Student> daoStudent, GenericDao<Oceny> daoOcena) {
        this.scanner = scanner;
        this.daoStudent = daoStudent;
        this.daoOcena = daoOcena;

    }

    public void wykonaj() {
        String komenda;
        do {
            System.out.println("Komenda [dodajS] [zwroc] [lista] [usun] [update] [dodajO] [usunO] [updateO] [srednia] [koniec]]");
            komenda = scanner.next();
            if (komenda.equalsIgnoreCase("dodajS")) {
                handleAddCommand();
            } else if (komenda.equalsIgnoreCase("zwroc")) { // znajdź studenta i wypisz jego informacje
                handleFindStudent();
            } else if (komenda.equalsIgnoreCase("lista")) {
                handleListCommand();
            } else if (komenda.equalsIgnoreCase("usun")) {
                handleDeleteCommand();
            } else if (komenda.equalsIgnoreCase("update")) {
                handleUpdateCommand();
            } else if (komenda.equalsIgnoreCase("dodajO")) {
                handleAddGrade();
            } else if (komenda.equalsIgnoreCase("usunO")) {
                handleDeleteGrade();
            }else if (komenda.equalsIgnoreCase("updateO")) {
                handleUpdateGrade();
            }else if (komenda.equalsIgnoreCase("srednia")) {
                handleAverageGrade();
            }
        } while (!komenda.equals("koniec"));
    }

    private void handleAverageGrade() {
        System.out.println("Podaj id studenta dla którego chcesz znac srednią ocen");
        Long idS = scanner.nextLong();

        Optional<Student> wskazanyStudent = daoStudent.znajdzPoId(idS, Student.class);
        if (wskazanyStudent.isPresent()) {
            Student studentDlaSredniej = wskazanyStudent.get();
            Set<Oceny> oceny = studentDlaSredniej.getOceny();
            Double suma = 0.0;
            Double iloscOcen = 0.0;
            for (Oceny ocena: oceny) {
                Double ocenaZprzedmiotu = ocena.getOcena();
                suma += ocenaZprzedmiotu;
                iloscOcen +=1;
            }
            Double srednia = suma/iloscOcen;

            System.out.println("Srednia studenta to " + srednia);


        }else{
            System.out.println("Nie ma takiego studenta");
        }
    }
    private void handleUpdateGrade() {
        System.out.println("Podaj id studenta dla którego chcesz poprawić ocenę");
        Long idS = scanner.nextLong();

        Optional<Student> wskazanyStudent = daoStudent.znajdzPoId(idS, Student.class);
        if (wskazanyStudent.isPresent()) {
            Student studentDlaOceny = wskazanyStudent.get();

            if(!czyPoprawionoOcene(studentDlaOceny)){
                System.out.println("Podaj id oceny, którą chcesz poprawić");
                Long idO = scanner.nextLong();

                Optional<Oceny> wskazanaOcena = daoOcena.znajdzPoId(idO, Oceny.class);
                if(wskazanaOcena.isPresent()){
                    Oceny ocena = wskazanaOcena.get();
                    System.out.println("Jak ocena po poprawce ?");
                    Double newOcena = scanner.nextDouble();
                    ocena.setOcena(newOcena);
                    daoOcena.aktualizuj(ocena);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 'godzina' h:mm a");
                    LocalDateTime dataPoprawki= LocalDateTime.now();
                    String formatDateTime = dataPoprawki.format(formatter);

                    ocena.setCzasPoprawki(dataPoprawki);
                    System.out.println("Dokonano porprawki oceny dla przedmiotu " + ocena.getPrzedmiot());
                    System.out.println("Po poprawie ocena to " + newOcena);
                    System.out.println("Poprawy dokonano " + formatDateTime);

                }else {
                    System.out.println("Nie ma takiego id dla szukanej oceny");
                }

            }else{
                System.out.println("Nie ma takiego studenta");
            }

            } else {
                System.out.println("Nie znaleziono takiego studenta");
            }
        }


    private void handleDeleteGrade() {
        System.out.println("Podaj id studenta dla którego chcesz usunąć ocenę");
        Long idS = scanner.nextLong();

        Optional<Student> wskazanyStudent = daoStudent.znajdzPoId(idS, Student.class);
        if (wskazanyStudent.isPresent()) {
            Student studentDlaOceny = wskazanyStudent.get();

            System.out.println("Podaj id oceny, którą chcesz usunąć");
            Long idO = scanner.nextLong();

            Optional<Oceny> wskazanaOcena = daoOcena.znajdzPoId(idO, Oceny.class);
            if(wskazanaOcena.isPresent()){
                Oceny ocena = wskazanaOcena.get();
                daoOcena.usun(ocena);
                System.out.println(ocena + " Ocena została usunięta " + "dla przedmiotu " + ocena.getPrzedmiot());

            }else {
                System.out.println("Nie ma takiego id dla szukanej oceny");
            }

        }else{
            System.out.println("Nie ma takiego studenta");
        }

    }

    private void handleAddGrade() {
        System.out.println("Podaj id studenta");
        Long id = scanner.nextLong();

        Optional<Student> wskazanyStudent = daoStudent.znajdzPoId(id, Student.class);
        if (wskazanyStudent.isPresent()) {
            Student studentDlaOceny = wskazanyStudent.get();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 'godzina' h:mm a");
            LocalDateTime dataCzasOcena = LocalDateTime.now();
            String formatDateTime = dataCzasOcena.format(formatter);
            System.out.println("Data i godzina wstawienia oceny: " + formatDateTime);

            System.out.println("Podaj ocenę");
            Double ocena = scanner.nextDouble();

            System.out.println("Podaj przedmiot");
            Przedmiot przedmiot = zaladujPrzedmiot();

            Oceny oceny = new Oceny(studentDlaOceny, dataCzasOcena, ocena, przedmiot);
            daoOcena.dodaj(oceny);
        }else{
            System.out.println("Nie ma takiego studenta");
        }
    }

    private Przedmiot zaladujPrzedmiot() {
            Przedmiot przedmiot = null;
            do {
                try {
                    System.out.println("Podaj przedmiot *POLSKI*, *ANGIELSKI*, *IT*, *MATMA*, *GEOGRAFIA*");
                    String unitString = scanner.next();

                    przedmiot = Przedmiot.valueOf(unitString.toUpperCase());
                } catch (IllegalArgumentException iae) {
                    System.err.println("Nie ma takiego podwozia");
                }
            } while (przedmiot == null);
            return przedmiot;
        }


    private void handleUpdateCommand() {
        System.out.println("Podaj id studenta");
        Long id = scanner.nextLong();

        Optional<Student> wskazanyStudent = daoStudent.znajdzPoId(id, Student.class);
        if (wskazanyStudent.isPresent()) {
            Student aktualizowanyStudent = wskazanyStudent.get();

            System.out.println("Co chciałbys poddac update? [imie, nazwisko, numerI, dataUr]");
            String output = scanner.next();
            switch (output) {
                case "imie":
                    System.out.println("Podaj imie studenta:");
                    String nazwa = scanner.next();

                    aktualizowanyStudent.setName(nazwa);
                    break;
                case "nazwiko":
                    System.out.println("Podaj nazwisko studenta:");
                    String nazwisko = scanner.next();

                    aktualizowanyStudent.setNazwisko(nazwisko);
                    break;
                case "numerI":
                    System.out.println("Podaj numer indeksu studenta:");
                    String numerIndeksu = scanner.next();

                    aktualizowanyStudent.setNumerIndeks(numerIndeksu);
                    break;
                case "dataUr":
                    String pattern = "yyyy-MM-dd";
                    LocalDate dataUrodzenia = zaladujDate(pattern);

                    aktualizowanyStudent.setDataUrodzenia(dataUrodzenia);
                    break;

                default:
                    System.out.println("Nie ma takiego studenta");
            }

            daoStudent.aktualizuj(aktualizowanyStudent);
            System.out.println("zaktualizowano dane studenta " + aktualizowanyStudent);
        } else {

            System.out.println("Nie ma takiego studenta");
        }
    }

    private void handleDeleteCommand() {
        System.out.println("Podaj id studenta");
        Long id = scanner.nextLong();

        Optional<Student> wskazanyStudent = daoStudent.znajdzPoId(id, Student.class);
        if (wskazanyStudent.isPresent()) {
            Student studentDoUsuniecia = wskazanyStudent.get();
            daoStudent.usun(studentDoUsuniecia);
            System.out.println("Usunięto studenta " + studentDoUsuniecia);

        } else {
            System.out.println("Wskazany student do usunięcia nie istnieje");
        }
    }


    private void handleListCommand() {
        List<Student> listaStudentow = daoStudent.list(Student.class);
        for (Student student : listaStudentow) {
            System.out.println("lista studentow " + student);
        }
        System.out.println();
    }
    private void handleFindStudent() {
        System.out.println("Podaj id studenta");
        Long id = scanner.nextLong();

        Optional<Student> wskazanyStudent = daoStudent.znajdzPoId(id, Student.class);
        if (wskazanyStudent.isPresent()) {
            Student student = wskazanyStudent.get();
        } else {
            System.out.println("Nie znaleziona takiego studenta");
        }
    }

    private void handleAddCommand() {
        System.out.println("Podaj imię studenta");
        String imie = scanner.next();

        System.out.println("Podaj nazwisko studenta");
        String nazwisko = scanner.next();

        System.out.println("Podaj numer indeksu");
        String numerIndeksu = scanner.next();

        System.out.println("Podaj datę urodzenia");
        String pattern = "yyyy-MM-dd";
        LocalDate dataurodzenia = zaladujDate(pattern);

        Student student = new Student(imie, nazwisko, numerIndeksu, dataurodzenia);
        daoStudent.dodaj(student);

    }

    private LocalDate zaladujDate(String pattern) {
        LocalDate dataUrodzenia = null;
        do {
            try {
                System.out.println("Podaj date urodzenia");
                String wprowadzonaDataWypozyczenia = scanner.next();
                DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(pattern);
                dataUrodzenia = LocalDate.parse(wprowadzonaDataWypozyczenia, FORMATTER);

                LocalDate today = LocalDate.now();
                if (dataUrodzenia.isAfter(today)) {
                    throw new IllegalArgumentException(("Chyba się jeszcze nie urodziłeś"));
                }

            } catch (IllegalArgumentException iae) {
                dataUrodzenia = null;
                System.err.println("Zły format daty, proszę trzymaj się schematu: yyy-MM-dd");
            }
        } while (dataUrodzenia == null);
        return dataUrodzenia;
    }

    private boolean czyPoprawionoOcene(Student student){
        Optional<Oceny> ocena = znajdzAktywnaOcena(student);
        return !ocena.isPresent();
    }
    private Optional<Oceny> znajdzAktywnaOcena(Student student) {

        if (student.getOceny().isEmpty()) {
            return Optional.empty();
        }

        for (Oceny ocena : student.getOceny()) {
            if (ocena.getCzasPoprawki() == null) { // sprawdzamy czy czasPoprawki został ustawiony
                return Optional.of(ocena);
            }
        }

        return Optional.empty();
    }



}
