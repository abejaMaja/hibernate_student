package pl.sda.arppl4.dziennik;

import pl.sda.arppl4.dziennik.dao.GenericDao;
import pl.sda.arppl4.dziennik.model.Student;
import pl.sda.arppl4.dziennik.parser.StudentParser;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GenericDao<Student> studentGenericDao = new GenericDao<>();



        StudentParser parser = new StudentParser(scanner, studentGenericDao);
        parser.wykonaj();
    }
}
