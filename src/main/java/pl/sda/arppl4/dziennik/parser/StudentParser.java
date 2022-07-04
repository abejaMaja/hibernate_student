package pl.sda.arppl4.dziennik.parser;

import pl.sda.arppl4.dziennik.dao.GenericDao;
import pl.sda.arppl4.dziennik.model.Oceny;
import pl.sda.arppl4.dziennik.model.Student;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class StudentParser {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Scanner scanner;
    private final GenericDao<Student> daoStudent;


    public StudentParser(Scanner scanner, GenericDao<Student> daoStudent) {
        this.scanner = scanner;
        this.daoStudent = daoStudent;

    }

    public void wykonaj() {
        String komenda;
        do {
            System.out.println("Komenda [dodaj] [zwroc] [lista] [usun] [update] [koniec]");
            komenda = scanner.next();
            if (komenda.equalsIgnoreCase("dodaj")) {
                handleAddCommand();
            } else if (komenda.equalsIgnoreCase("zwroc")) { // znajdź studenta i wypisz jego informacje
                handleFindStudent();
            } else if (komenda.equalsIgnoreCase("lista")) {
                handleListCommand();
            } else if (komenda.equalsIgnoreCase("usun")) {
                handleDeleteCommand();
            } else if (komenda.equalsIgnoreCase("update")) {
                handleUpdateCommand();
            }
        } while (!komenda.equals("koniec"));
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
                    LocalDate dataUrodzenia = zaladujDateUrodzenia();

                    aktualizowanyStudent.setDataUrodzenia(dataUrodzenia);
                    break;

                default:
                    System.out.println("Nie ma takiego studenta");
            }

            daoStudent.aktualizuj(aktualizowanyStudent);
            System.out.println("zaktualizowano dane studenta " + aktualizowanyStudent);
        } else {

            System.out.println("Nie znaleziono samochodu");
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
        LocalDate dataurodzenia = zaladujDateUrodzenia();

        Student student = new Student(imie, nazwisko, numerIndeksu, dataurodzenia);
        daoStudent.dodaj(student);

    }

    private LocalDate zaladujDateUrodzenia() {
        LocalDate dataUrodzenia = null;
        do {
            try {
                System.out.println("Podaj date urodzenia");
                String wprowadzonaDataWypozyczenia = scanner.next();

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




}
